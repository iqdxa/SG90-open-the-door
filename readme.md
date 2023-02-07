# 舵机开门

<p align="center">
	<img src="https://img.shields.io/badge/language-kotlin-orange.svg"/>
	<img src="https://img.shields.io/github/repo-size/iqdxa/SG90-open-the-door">
	<img src="https://img.shields.io/github/v/release/iqdxa/SG90-open-the-door">
	<img src="https://img.shields.io/github/downloads/iqdxa/SG90-open-the-door/total">
<br>
    <a href="https://iqdxa.github.io/SG90-open-the-door">主页</a>|
    <a href="https://github.com/iqdxa/SG90-open-the-door/releases">更新日志</a>
</p>



## 想法来源

有一天在哔哩哔哩上面看到了有人用Arduino和舵机控制开门，就感觉很新奇，就想着自己也来试一试看看能不能成。

## 项目介绍

### TODO

#### 硬件

- [ ] 添加遥控器控制功能
- [ ] 增加蜂鸣器以提示状态

#### 软件

- [ ] 开门次数统计
- [ ] 增加上下文菜单：复制

### 硬件介绍

希望能够使用手机远程舵机控制开门，并有语音提示，如果可以使用红外遥控器也行。因为有电源，所以无需考虑耗电问题。

#### 舵机

##### 舵机接线方法

红-------------------------VCC

棕色----------------------GND

橙色----------------------信号线

![](photo\舵机接线颜色示意图.png)

##### 控制舵机

采用PWM控制的方式来进行舵机的操纵，舵机的控制需要MCU产生一个20ms的脉冲信号，以0.5ms到2.5ms的高电平来控制舵机的角度。对于180°舵机的控制数据如下：

- 0.5ms-------------0度； 2.5% 对应函数中占空比为250μs
- 1.0ms------------45度； 5.0% 对应函数中占空比为500μs
- 1.5ms------------90度； 7.5% 对应函数中占空比为750μs
- 2.0ms-----------135度； 10.0% 对应函数中占空比为1000μs
- 2.5ms-----------180度； 12.5% 对应函数中占空比为1250μs

以C51单片机控制舵机为例，利用定时中断定时500μs，每触发40次中断为一个PWM的周期（20ms），在中断程序里控制引脚的输出时间，达到模拟PWM的效果控制舵机。

#### 元器件清单

- HC-05蓝牙模块
- 红外接收模块
- STC89C52RC
- 蜂鸣器
- SG-90舵机
- ~~热敏电阻~~
- ~~光敏电阻~~

### 软件介绍

希望能够通过手机的蓝牙远程控制单片机进行开门。

软件使用GitHub用户Shanyaliux的SerialPortSample开源项目中的kotlindemo应用，并自己进行修改以适应自己使用。[软件下载地址]([Releases · iqdxa/SG90-open-the-door (github.com)](https://github.com/iqdxa/SG90-open-the-door/releases))

#### 软件使用

1. 连接蓝牙设备
2. 选择蓝牙
3. 点击开门
4. 双击返回键退出程序

#### 感叹
​		因为弄软件更新相关内容，有数据请求的过程，就存在多线程问题，最开始用的Retrofit，结果开启了多线程，不能改变UI，弄了很久，花了很多时间。后面改成使用okhttp，还是花了很多时间去处理，最终才大概解决了问题，但是还不是很满意，不能实现在关于界面点击“检测更新”实现更新。也许这次过后，对多线程都有恐惧感了。——2023-02-04

## 总结

​		之前在淘宝上买了一些小玩具，什么都是别人做好的，照着做就行，就很简单。现在自己弄，才发现想法都很好，要实现起来就有难度，就只好删了很多功能。本来想弄一些新增的模块，想着可以直接从之前的文件copy，但是发现单片机不是同一个，copy以后就报错，而我又不懂，也就只有放弃新增加模块。热敏电阻和光敏电阻都不会，还是删了吧。

​		之前用Arduino，使用舵机就非常方便，现在用51单片机，才发现舵机实现起来很麻烦，就在网上找的代码（[C51——PWM控制舵机 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/491642595)），才把舵机控制解决了。但是因为这个，我知道了单片机中断的相关内容，也算是有收获。51单片机蓝牙模块接收数据的代码也是来自网上（[51单片机串口通信的发送与接收 字符串_IT和尚的博客-CSDN博客_51串口通信发送字符串](https://blog.csdn.net/u013040887/article/details/88377326)），自己做了修改。

​		想自己通过Maui写一个安卓程序来实习手机蓝牙控制，但是能力不足，还是使用Android Studio借鉴别人的代码自己修改一个适合自己使用的安卓应用。

​		在别人的基础上进行安卓应用开发，确实方便了很多，可以直接使用不用接触蓝牙相关的代码，而且从中还学到了很多相关的知识。自己同时也在微信读书上看《第一行代码（第三版）》的内容，对安卓开发也有了一些了解，但是很多东西都记不住。

​		通过这次项目，对项目开发有了很多了解，但是自己也只是随便玩玩，还是有很多的不足，要学的内容还有很多。

## 感谢

### 开源项目

- [SerialPortSample](https://github.com/Shanyaliux/SerialPortSample)

- [AppUpdate](https://github.com/azhon/AppUpdate)

- [android-about-page](https://github.com/medyo/android-about-page)

### 网站

[C51——PWM控制舵机 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/491642595)

[51单片机串口通信的发送与接收 字符串_IT和尚的博客-CSDN博客_51串口通信发送字符串](https://blog.csdn.net/u013040887/article/details/88377326)
