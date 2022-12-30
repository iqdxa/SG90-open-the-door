#include <reg52.h>
#include "intrins.h"

#define uchar unsigned char
#define uint unsigned int

sbit pwm = P1^0;
sbit sw1 = P3^4;
sbit sw2 = P3^5;

//TODO:��ÿ����Ӧ��ֵ��ȣ���angle�Ƕ��پ��Ƕ��ٶ�
//5��Ӧ0�� 10��Ӧ45�� 15��Ӧ90�� 20��Ӧ135�� 25��Ӧ180��
uint angle=15;//������ʼ�Ƕ�Ϊ90��
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
	TMOD = 0x01;//��ʱ���㹤����ʽ1
	TL0 = 0xA3;
	TH0 = 0xFF;//��ʼֵ500us
	ET0 = 1;
	TF0 = 0;//���TF0��־
	TR0 = 1;//��ʱ����ʼ��ʱ
	EA = 1;//�ж��ܿ���
}

void main()
{
	count=0;
	Init();//��ʼ��
	while(1)
	{
		if(sw1==0)
		{
			delay(10);//����
			if(sw1==0)//�ٴ�ȷ��
			{
				angle++;
				count=0;
				if(angle==25)angle=25;
				while(sw1==0);//ֱ�����ֲ�����ѭ��
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

void Timer() interrupt 1 //�жϳ���pwm�ź����
{
	TL0 = 0xA3;
	TH0 = 0xFF;

	count++;
	if(count == 20)count = 0;
	if(count < angle)pwm = 1;
	else pwm = 0;
}
