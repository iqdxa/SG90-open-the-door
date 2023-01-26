#include <reg52.h>
#include "intrins.h"

#define uchar unsigned char
#define uint unsigned int

sbit pwm = P1^0;
sbit sw1 = P3^2;
sbit sw2 = P3^3;
bit New_rec = 0, Send_ed = 1;
uint UART_buff;
//TODO:让每个对应的值相等，即angle是多少就是多少度
//5对应0度 10对应45度 15对应90度 20对应135度 25对应180度
uint angle=0;//调整初始角度为0°
uint count=0;
int i=0;

void delay(uint ms)
{
	uint i, j;
	for(i=0;i<ms;i++)
		for(j=0;j<112;j++);
}

void Init()
{
	//Time0_init
	TMOD |= 0x01;//定时器零工作方式1
	TL0 = 0xA3;
	TH0 = 0xFF;//初始值500us
	ET0 = 1;
	TF0 = 0;//清除TF0标志
	TR0 = 1;//定时器开始计时
	
	//接收中断初始化
	SCON=0x50;//设定串口工作方式0101 0000
	PCON=0x00;
	TMOD |=0x20;
	TL1=0xfd;//波特率9600
	TH1=0xfd;
	TR1=1;
	
	
	EA = 1;
	ES  = 1;       //开中断
	
}

void main()
{
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

void ser_int (void) interrupt 4
{
	if(RI == 1) 
	{
		ES=0;		//暂时关闭串口中断
		//如果收到.
		RI = 0;      //清除标志.
		New_rec = 1;
		UART_buff = SBUF;  //把收到的信息从SBUF放到UART_buff中
		if(UART_buff == 1 || UART_buff==0x31)  
		{
			sw1=0;
			sw2=1;
		}
		else if(UART_buff == 0 || UART_buff==0x30)
		{
			sw1=1;
			sw2=0;
		}
	}
	else
	{        //如果送毕.
      TI = 0;      //清除标志.
      Send_ed = 1;
    }
	ES=1;//开启串口中断
}