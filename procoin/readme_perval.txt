1、xml命名

规则：类型_形状_属性

例子1: 圆角、颜色填充、边框
无点击效果、带3dp圆角、颜色填充、3dp边框的矩形 -> shape_rect_corner3_sld_ffffff_stk3_aaaaaa (sld:solid , stk:stroke)

例子2: 圆角、颜色填充
无点击效果、带3dp圆角、颜色填充的矩形 -> shape_rect_corner3_solid_ffffff   (四个角都是圆角直接写corner+圆角大小)

例子3: 左上圆角、右上圆角、边框
无点击效果、左上、右上带3dp圆角、颜色填充的矩形 -> shape_rect_lt3_rt3_solid_ffffff (lt:left top , rt:right:top)

例子4: 左下圆角、右下圆角、边框
无点击效果、左下、右下带3dp圆角、颜色填充的矩形 -> shape_rect_lb3_rb3_solid_ffffff (lb:left bottom , rb:right bottom)

例子5: 非圆角、边框
带点击效果、非圆角、3dp边框矩形   -> selector_rect_stroke3_ffffff


2、图片命名

规则：图片前缀_模块_子模块_图片功能描述

网红_首页_播放按钮 -> ic_olstar_home_videoplay
网红_交易_搜索按钮 -> ic_olstar_home_search
邮币_登录_登录按钮背景 -> ic_stamp_home_login_btn
邮币_登录_分享按钮图片 -> ic_stamp_home_share_btn
邮币_登录_热门图标 -> ic_stamp_home_hot_img 

转换成共用,模块/子模块用common替代 -> ic_common_videoplay


3、布局命名
规则：模块名_功能描述_组件

网红收益排行榜activity布局 -> olstar_profit_rank_activity
网红收益排行榜listview item -> olstar_profit_rank_lvitem
网红收益排行榜提示Dialog -> olstar_profit_rank_dialog

转换成共用,模块/子模块用common替代 -> common_listview


4、xml动画命名（动画类型根据动画xml跟根标签确定，如animation-list set translate alpha）

4.1、动画效果跟业务强耦合，不可公用（通常是一组图片的固定集合，如语音播放的动画）
规则：动画类型_模块功能描述

聊天语音播放动画 -> animation_list_voice_play

4.2、动画效果具有普遍性，可公用
规则：动画类型_动画描述

透明度300毫秒内从0.5变为1 -> alpha_from0_5_to1_dur300
100毫秒内沿X轴从40平移到900 -> translate_fromx40_tox900_dur100
500毫秒内沿X轴从5平移到9、沿Y轴从100平移到700 -> translate_fx5_tx9_fy100_ty700  (fx:是fromx的缩写 ，当x、y坐标同时变化时用缩写，f：form t:to)


少变在前，多变在后








