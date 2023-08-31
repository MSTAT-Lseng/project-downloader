# Interpreter 解释器
用于解析搜索源并显示给用户。

## 界面截图
![截图1](截图.png)

## 简易流程图
![流程图](解释器流程图.png)

## 包含
1. 解释器本体（提供框架）
2. 搜索源模块（提供内容）

## 解释器本体
 - assets/module_scriptor/：为搜索源脚本提供的API。
 - assets/search_list_modules/：搜索源脚本配置目录。
 - res/：所需的图像、布局以及预置文件。
 - java/.../Config.java：基础配置空间。
 - java/.../utils/：提供支持的类。包括文件操作、Intent跳转、UI配置、稍后再看（在本开源模组中未提供实质性功能）和颜色组件库。
 - java/.../module_scriptor/ModuleOpener.java：启动解释器的核心API类。
 - java/.../module_scriptor/module_opener/：解析 Manifest.json 的相关类。
 - java/.../module_scriptor/common_onlinevideo/SearchTaskerActivity：搜索结果列表（截图左）。
 - java/.../module_scriptor/common_onlinevideo/DetailsBrowseActivity：番剧详细信息（截图中）。
 - java/.../module_scriptor/common_onlinevideo/ContentPlayerActivity：动画播放页面（截图右）。
 - java/.../module_scriptor/common_onlinevideo/CookieIdentityActivity：Cookie认证页面（需要验证码才能访问的搜索源调用）。
 - java/.../module_scriptor/common_onlinevideo/ResourceTrackerActivity：捕获视频资源链接显示页面。
 - java/.../module_scriptor/common_onlinevideo/scriptor/：解释相关搜索源JSON文件


## 启动搜索源
 `showTaskSearchlistModuleResult(activity, ModuleOpener.openAssetsModule(activity, "搜索源路径", "搜索内容关键词"))`

## 搜索源模块
 - Manifest.json：搜索源基础信息（搜索源名称、搜索源网址、搜索源类型等）
 - search_tasker_config.json：搜索源搜索配置（关键词提交方式、搜索页面网址等）
 - search_tasker.js：搜索源处理脚本（启动 data 变量为获取的数据）。
 - browse_parser.js：搜索源番剧详情处理脚本。
 - player_scriptor.js：搜索源播放页面脚本（可隐藏某些页面内容）。

## 其他
 请参考相关开源代码，更多信息正在补充。
