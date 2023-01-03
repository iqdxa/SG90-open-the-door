#include <reg52.h>
#include "intrins.h"

#define uchar unsigned char
#define uint unsigned int

sbit pwm = P1^0;
sbit sw1 = P3^1;
sbit sw2 = P3^0;

//TODO:让每个对应的值相等，即angle是多少就是多少度
//5对应0度 10对应45度 15对应90度 20对应135度 25对应180度
uint angle=0;//调整初始角度为0°
uint count=0;

void delay(uint ms)
{
	uint i, j;
	for(i=0;i<ms;i++)
		for(j=0;j<112;j++);
}

void Init()
{
	//Time0_init
	TMOD = 0x01;//定时器零工作方式1
	TL0 = 0xA3;
	TH0 = 0xFF;//初始值500us
	ET0 = 1;
	TF0 = 0;//清除TF0标志
	TR0 = 1;//定时器开始计时
	EA = 1;//中断总开关
}

void main()
{
	int i=0;
	Init();//初始化
	while(1)
	{
		if(sw1==0)
		{
			//当angle=180时，只能按sw2，按sw1不起作用
			if(angle<180)
			{
				for(i=0;i<=180;i++)
				{
					angle=i;
					delay(2);
				}
			}
			count=0;
			while(sw1==0);//直到松手才跳出循环
		}
		if(sw2==0)
		{
			//当angle=0时，只能按sw1，按sw2不起作用
			if(angle>0)
			{
				for(i=180;i>=0;i--)
				{
					angle=i;
					delay(2);
				}
			}
			if(angle<=0)angle=0;
			count=0;
			while(sw2==0);
		}
	}
}

void Timer() interrupt 1 //中断程序pwm信号输出
{
	TL0 = 0xA3;
	TH0 = 0xFF;

	count++;
	if(count == 20)count = 0;
	if(count < angle/6)pwm = 1;
	else pwm = 0;
}
