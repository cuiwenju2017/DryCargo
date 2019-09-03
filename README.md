# 所用到的依赖库:

## 二级标题
### 三级标题
#### 四级标题
##### 五级标题
###### 六级标题
二、编辑基本语法
1、字体格式强调
 我们可以使用下面的方式给我们的文本添加强调的效果
*强调*  (示例：斜体)
 _强调_  (示例：斜体)
**加重强调**  (示例：粗体)
 __加重强调__ (示例：粗体)
***特别强调*** (示例：粗斜体)
___特别强调___  (示例：粗斜体)
2、代码
`<hello world>`

3、代码块高亮
```
@Override
protected void onDestroy() {
    EventBus.getDefault().unregister(this);
    super.onDestroy();
}
```
4、表格 （建议在表格前空一行，否则可能影响表格无法显示）

 表头  | 表头  | 表头
 ---- | ----- | ------
 单元格内容  | 单元格内容 | 单元格内容
 单元格内容  | 单元格内容 | 单元格内容

5、其他引用
图片
![图片名称](https://www.baidu.com/img/bd_logo1.png)
链接
[链接名称](https://www.baidu.com/)
6、列表
1. 项目1
2. 项目2
3. 项目3
   * 项目1 （一个*号会显示为一个黑点，注意⚠️有空格，否则直接显示为*项目1）
   * 项目2

7、换行（建议直接在前一行后面补两个空格）
直接回车不能换行，
可以在上一行文本后面补两个空格，
这样下一行的文本就换行了。
或者就是在两行文本直接加一个空行。
也能实现换行效果，不过这个行间距有点大。

8、引用
> 第一行引用文字
> 第二行引用文字
————————————————
版权声明：本文为CSDN博主「snowzhao210」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/qq_31796651/article/details/80803599


//rxjava引入的包
implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'
implementation 'io.reactivex.rxjava2:rxjava:2.1.7'
//retrofit引入的包
implementation 'com.squareup.retrofit2:retrofit:2.3.0'
implementation 'com.google.code.gson:gson:2.2.4'
//返回给我的是一个json解析后的bean
implementation 'com.squareup.retrofit2:converter-gson:2.1.0'
//rxjava2+retrofit搭配使用的依赖
implementation 'com.squareup.retrofit2:adapter-rxjava:2.3.0'
//okhttp日记拦截器
implementation 'com.squareup.okhttp3:logging-interceptor:3.9.1'
//沉浸状态栏
implementation 'com.jaeger.statusbarutil:library:1.5.1'
implementation 'com.android.support:design:28.0.0'
//图片圆角库
implementation 'com.github.SheHuan:NiceImageView:1.0.5'
//RecyclerAdapter框架
implementation 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.30'
//图片加载库
implementation 'com.github.bumptech.glide:glide:3.7.0'
//下拉刷新上拉加载更多
implementation 'com.scwang.smartrefresh:SmartRefreshLayout:1.0.5.1'
//photoview图片预览库
implementation('com.github.chrisbanes.photoview:library:+') {
    exclude group: 'com.android.support'
}