package com.sunny.module.stu.A基础知识

class R代码管理 {

    private fun stu_git() {
        /*
        查看远程仓库：
            git remote -v
        分支合并：
            git merge：
                这个命令将两个分支的更改集成到一起。如果两个分支有冲突的更改，Git 会在合并过程中创建一个新的 "merge commit" 来解决这些冲突。
                这个 "merge commit" 有两个父提交，分别是你正在合并的两个分支的最新提交。
                结果是一个包含所有更改的单一提交，但这种方式可能会使你的提交历史变得复杂，特别是在多人协作的项目中。
            git rebase：
                这个命令的目标是创建一个 "干净" 的提交历史。它会把你的本地更改 "移动" 到你要 rebase 的分支（通常是主分支）的最新更改之后。
                换句话说，它会改变你的本地更改的基础，使其基于最新的远程更改。这样做的结果是你的提交历史会更加清晰和线性，因为它不包含那些解决冲突的 "merge commit"。
                然而，这种方法可能会更难解决冲突，因为你需要在 rebase 过程中一次解决所有的冲突。
        拉取远程仓库代码：
            git pull(git fetch + git merge)：
            git pull --rebase(git fetch + git rebase)：
        提交当前代码到指定分支：
            git push origin HEAD:refs/for/df91_dev
        合并某个提交到当前分支：
            git cherry-pick 9fceb02

        回退代码：
            git stash // 将"工作区"没有提交的更改保存到"暂存区"
            git stash pop // 将"暂存区"最近的一次更改回复到"工作区"
            git reset HEAD^ // reset 最近一次提交
            git reset HEAD~n // reset 最近N次提交
        查看Git头指针(HEAD)的移动历史
            git reflog
         */
        // 工作区，暂存区，Git仓库

    }
}