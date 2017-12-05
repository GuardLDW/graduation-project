package com.bjut.cyl.kfyrip.utils;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * 作者：haoran   on https://github.com/woaigmz 2017/6/1.
 * 邮箱：1549112908@qq.com
 * 说明：打开文件
 */

public class OpenFiles {

    //打开预览文档的其他app工具
    public static Intent getPreviewFileIntent(File file,String type)
    {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, type);
        return intent;
    }

}
