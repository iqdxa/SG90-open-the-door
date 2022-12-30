#include<reg52.h>
#include<dingshiqi.h>

void Timer0_Init()
{
	TMOD = 0x01;//定时器零工作方式1
    TL0 = 0xA3;
    TH0 = 0xFF;//初始值500us
    ET0 = 1;
    TF0 = 0;//清除TF0标志
    TR0 = 1;//定时器开始计时
    EA = 1;//中断总开关
}