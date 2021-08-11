package com.sunny.module.stu.GLinux系统;

import com.sunny.module.stu.base.StuImpl;

/*
https://www.cnblogs.com/adforce/p/3522433.html
 */
public class Stu_inode extends StuImpl {

    @Override
    public void a_是什么() {
        // 索引节点，存储文件的【元信息】，通过元信息可以定位到文件的物理块
        /*
            每一个文件都有对应的inode，里面包含了与该文件有关的一些信息
         */

        // inode大小
        /*
            inode也会消耗硬盘空间，所以硬盘格式化的时候，操作系统自动将硬盘分成两个区域。
                一个是数据区，存放文件数据；
                另一个是inode区（inode table），存放inode所包含的信息。

            每个inode节点的大小，一般是128字节或256字节。
            inode节点的总数，在格式化时就给定，一般是每1KB或每2KB就设置一个inode。
                假定在一块1GB的硬盘中，每个inode节点的大小为128字节，每1KB就设置一个inode，
                那么inode table的大小就会达到128MB，占整块硬盘的12.8%。

            由于每个文件都必须有一个inode，因此有可能发生inode已经用光，但是硬盘还未存满的情况。这时，就无法在硬盘上创建新文件。
         */

        // inode号码
        /*
            每个inode都有一个号码，操作系统用inode号码来识别不同的文件。
                这里值得重复一遍，Unix/Linux系统内部不使用文件名，而使用inode号码来识别文件。
                对于系统来说，文件名只是inode号码便于识别的别称或者绰号。
            表面上，用户通过文件名，打开文件。实际上，系统内部这个过程分成三步：
                首先，系统找到这个文件名对应的inode号码；
                其次，通过inode号码，获取inode信息；
                最后，根据inode信息，找到文件数据所在的block，读出数据。
         */

        // 目录文件
        /*
            Unix/Linux系统中，目录（directory）也是一种文件。
                打开目录，实际上就是打开目录文件。

            目录文件的结构非常简单，就是一系列目录项（direct）的列表。
                每个目录项，由两部分组成：
                    所包含文件的文件名，
                    以及该文件名对应的inode号码。
         */

        // 权限
        /*
            理解了上面这些知识，就能理解目录的权限。目录文件的读权限（r）和写权限（w），都是针对目录文件本身。

            由于目录文件内只有文件名和inode号码，所以如果只有读权限，只能获取文件名，无法获取其他信息，
                因为其他信息都储存在inode节点中，而读取inode节点内的信息需要目录文件的执行权限（x）。
         */

        //硬链接
        /*
            一般情况下，文件名和inode号码是"一一对应"关系，每个inode号码对应一个文件名。
                但是，Unix/Linux系统允许，多个文件名指向同一个inode号码。

                这意味着，可以用不同的文件名访问同样的内容；对文件内容进行修改，会影响到所有文件名；
                    但是，删除一个文件名，不影响另一个文件名的访问。这种情况就被称为"硬链接"（hard link）。
                ln命令可以创建硬链接：ln 源文件 目标文件
         */

        // 软链接
         /*
            文件A和文件B的inode号码虽然不一样，但是文件A的内容是文件B的路径。
                读取文件A时，系统会自动将访问者导向文件B。
            因此，无论打开哪一个文件，最终读取的都是文件B。这时，文件A就称为文件B的"软链接"（soft link）
                或者"符号链接（symbolic link）

            ln -s命令可以创建软链接：ln -s 源文文件或目录 目标文件或目录
         */

        // inode的特殊作用
        /*
        由于inode号码与文件名分离，这种机制导致了一些Unix/Linux系统特有的现象：
            (1) 有时，文件名包含特殊字符，无法正常删除。这时，直接删除inode节点，就能起到删除文件的作用。
            (2) 移动文件或重命名文件，只是改变文件名，不影响inode号码。
            (3) 打开一个文件以后，系统就以inode号码来识别这个文件，不再考虑文件名。
                因此，通常来说，系统无法从inode号码得知文件名。

        第3点使得软件更新变得简单，可以在不关闭软件的情况下进行更新，不需要重启。
            因为系统通过inode号码，识别运行中的文件，不通过文件名。
            更新的时候，新版文件以同样的文件名，生成一个新的inode，不会影响到运行中的文件。
            等到下一次运行这个软件的时候，文件名就自动指向新版文件，旧版文件的inode则被回收。
         */
    }

    @Override
    public void s_数据结构() {
        /*
        inode包含文件的元信息，具体来说有以下内容：
            * Size：         文件的字节数
            * Uid：          文件拥有者的User ID
            * Gid：          文件的Group ID
            * Access：       文件的读、写、执行权限
            * 文件的时间戳，共有三个：
                ctime Change：指inode上一次变动的时间，
                mtime Modify：指文件内容上一次变动的时间，
                atime Access：指文件上一次打开的时间。
            * Links：链接数，即有多少文件名指向这个inode
            * 文件数据block的位置
         */
    }

    @Override
    public void d_怎么用() {
        // stat xxx     // 查看某个文件的元信息
        /*
root@X4_40:/ # stat ueventd.rc
  File: `ueventd.rc'
  Size: 4650	 Blocks: 16	 IO Blocks: 4096	regular file
Device: 1h	 Inode: 3207	 Links: 1
Access: (644/-rw-r--r--)	Uid: (0/    root)	Gid: (0/    root)
Access: 1970-01-01 08:00:03.255999999
Modify: 1970-01-01 08:00:00.000000000
Change: 1970-01-01 08:00:01.031999999
         */

        // df -i        // 查看每个硬盘分区的inode总数和已经使用的数量
        /*
zhangxin:~ qazwsx$ df -i
Filesystem    512-blocks      Used Available Capacity iused      ifree %iused  Mounted on
/dev/disk0s2   488555536 441717112  46326424    91% 3442892 4291524387    0%   /
devfs                379       379         0   100%     656          0  100%   /dev
map -hosts             0         0         0   100%       0          0  100%   /net
map auto_home          0         0         0   100%       0          0  100%   /home
         */

        // ls -i xxx    // 查看文件名对应的inode号码
        /*
root@X4_40:/ # ls -i ueventd.rc
    3207 ueventd.rc
         */
    }
}
