- 从数据库查询一二级菜单是否显示时，二级菜单的查询代码写的位置是否可以更改
  - src/store/modules/menu.js
  - ![image-20201101181353654](onlinehospital.assets/image-20201101181353654.png)
- 后续字典管理统称改为码表管理，需要改一些数据显示内容
- 字典管理里面查看某个字典的码值内容时，改成模态框弹出形式
- 关于字典类型的添加和修改的检查是否存在逻辑，总感觉不太对
  - ![image-20201103113425692](onlinehospital.assets/image-20201103113425692.png)
- SimpleUser类，原来是写的private Serializable userId;太奇怪了，先改成Long类型了，后续研究
  - ![image-20201103133733900](onlinehospital.assets/image-20201103133733900.png)

