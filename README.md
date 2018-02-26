# RBmob
Bmob后端数据sdk接入 2018-2-23

>BmobObject类本身包含objectId、createdAt、updatedAt、ACL四个默认的属性，objectId是数据的唯一标示，相当于数据库中表的主键，createdAt是数据的创建时间，updatedAt是数据的最后修改时间，ACL是数据的操作权限。

 // 仅在客户端使用，不希望被gson序列化提交到后端云，记得用transient修饰
>private transient Integer count;

### 特殊对象
为了提供更好的服务，BmobSDK中提供了BmobUser、BmobInstallation、BmobRole三个特殊的BmobObject对象来完成不同的功能，在这里我们统一称为特殊对象。

`BmobUser`对象主要是针对应用中的用户功能而提供的，它对应着web端的User表，使用BmobUser对象可以很方便的在应用中实现用户的注册、登录、邮箱验证等功能，具体的使用方法可查看文档的用户管理部分。

`BmobInstallation`对象主要用于应用的安装设备管理中，它对应着web端的Installation表，任何安装了你应用的设备都会在此表中产生一条数据标示该设备。结合Bmob提供的推送功能，还可以实现将自定义的消息推送给不同的设备终端，具体的使用方法可查看文档的消息推送部分。

`BmobRole`对象主要用于角色管理，对应用于Web端的Role表，具体的使用方法可查看文档的ACL和角色部分。

### [数据类型](http://docs.bmob.cn/data/Android/b_developdoc/doc/index.html#%E6%95%B0%E6%8D%AE%E7%B1%BB%E5%9E%8B)
目前为止，Bmob支持的数据类型：String、Integer、Float、Short、Byte、Double、Character、Boolean、Object、Array。
同时也支持BmobObject、BmobDate、BmobGeoPoint、BmobFile特有的数据类型。

以下为Web端类型与SDK端支持的JAVA类型对应表：

Web端类型	支持的JAVA类型	说明
Number	Integer、Float、Short、Byte、Double、Character	对应数据库的Number类型
Array	List	数组类型
File	BmobFile	Bmob特有类型，用来标识文件类型
GeoPoint	BmobGeoPoint	Bmob特有类型，用来标识地理位置
Date	BmobDate	Bmob特有类型，用来标识日期类型
Pointer	特定对象	Bmob特有类型，用来标识指针类型
Relation	BmobRelation	Bmob特有类型，用来标识数据关联
注：不能使用int、float、short byte、double、character等基本数据类型。`

### 2018-2-23
BmobSDK_3.5.9_20180102

接入文档地址: http://doc.bmob.cn/data/android/