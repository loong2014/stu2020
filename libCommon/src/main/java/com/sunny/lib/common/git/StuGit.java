package com.sunny.lib.common.git;

public class StuGit {
    /**
     * https://blog.csdn.net/fenfeidexiatian/article/details/95308119
     *
     * git本地仓库，如果长时间不进行清理，几个月以后的某一天，可能拉取代码的时候突然提示你
     *
     * git Auto packing the repository in background for optimum performance. See "git help gc" for manu...
     * 查资料，原来是自己本地一些 “悬空对象”太多(git删除分支或者清空stash的时候，这些其实还没有真正删除，成为悬空对象，
     * 我们可以使用merge命令可以从中恢复一些文件)，话不多说，先上解决方法吧~！
     *
     * 解决方法：
     * 1.输入命令：git fsck --lost-found，可以看到好多“dangling commit”
     * 2.清空他们：git gc --prune=now，完成
     */
    private void stu(){

    }
}
