#include <reg52.h>
#include "intrins.h"

#define uchar unsigned char
#define uint unsigned int

sbit pwm = P1^0;
sbit sw1 = P3^4;
sbit sw2 = P3^5;

//TODO:让每个对应的值相等，即angle是多少就是多少度
//5对应0度 10对应45度 15对应90度 20对应135度 25对应180度
uint angle=15;//调整初始角度为90°
uint count;

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
	count=0;
	Init();//初始化
	while(1)
	{
		if(sw1==0)
		{
			delay(10);//消抖
			if(sw1==0)//再次确认
			{
				angle++;
				count=0;
				if(angle==25)angle=25;
				while(sw1==0);//直到松手才跳出循环
			}
		}
		if(sw2==0)
		{
			delay(10);
			if(sw2==0)
			{
				angle--;
				count=0;
				if(angle==5)angle=5;
				while(sw2==0);
			}
		}
	}
	while(1);
}

void Timer() interrupt 1 //中断程序pwm信号输出
{
	TL0 = 0xA3;
	TH0 = 0xFF;

	count++;
	if(count == 20)count = 0;
	if(count < angle)pwm = 1;
	else pwm = 0;
}
