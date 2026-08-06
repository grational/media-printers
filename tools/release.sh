#!/usr/bin/env bash
set -euo pipefail

readonly prefix=v
readonly main_branch=master
readonly username=grational

usage() {
	echo "${0##*/} [version]" >&2
	exit 1
}

platform_password() {
	local remote_platform
	remote_platform="$(git remote get-url origin | cut -d/ -f3)"
	bw list items --search "$remote_platform" \
		| jq -j '.[0].fields[] | select(.name == "token") | .value'
}

release() {
	local password="$1"
	local version="$2"
	local -a args=(
		release
		"-Prelease.customUsername=$username"
		"-Prelease.customPassword=$password"
	)

	if [[ -n $version ]]; then
		args+=("-Prelease.version=$version")
	fi

	./gradlew "${args[@]}"
}

latest_main_version() {
	local tag

	while IFS= read -r tag; do
		if [[ $tag != *-* ]]; then
			echo "${tag#"$prefix"}"
			return
		fi
	done < <(git tag --merged "$main_branch" --sort=-v:refname)

	echo 'No main release tag found' >&2
	return 1
}

restore_branch() {
	if [[ -d $(git rev-parse --git-path rebase-merge) || -d $(git rev-parse --git-path rebase-apply) ]]; then
		git rebase --abort
	fi
	git switch "$original_branch"
}

if (( $# > 1 )); then
	usage
fi

if [[ -n $(git status --porcelain) ]]; then
	echo 'The worktree must be clean before releasing' >&2
	exit 1
fi

original_branch="$(git branch --show-current)"
readonly original_branch
if [[ -z $original_branch ]]; then
	echo 'Cannot release from a detached HEAD' >&2
	exit 1
fi
trap restore_branch EXIT

version="${1:-}"
version="${version#"$prefix"}"
password="$(platform_password)"
readonly password

git switch "$main_branch"
release "$password" "$version"

if [[ -z $version ]]; then
	version="$(latest_main_version)"
fi

declare -a backports=(
	"groovy-4:g4:true"
	"java-8-groovy-3:j8g3:false"
)

for entry in "${backports[@]}"; do
	IFS=: read -r branch postfix rebase <<< "$entry"

	if [[ $rebase == true ]]; then
		git rebase "$main_branch" "$branch"
		git push --force-with-lease origin "${branch}:${branch}"
	else
		git switch "$branch"
		git push origin "${branch}:${branch}"
	fi

	release "$password" "$version-$postfix"
done
