#include <reg52.h>
#include "intrins.h"

#define uchar unsigned char
#define uint unsigned int

sbit pwm = P1^0;
sbit sw1 = P3^2;
sbit sw2 = P3^3;
bit New_rec = 0, Send_ed = 1;
uint UART_buff;
//TODO:��ÿ����Ӧ��ֵ��ȣ���angle�Ƕ��پ��Ƕ��ٶ�
//5��Ӧ0�� 10��Ӧ45�� 15��Ӧ90�� 20��Ӧ135�� 25��Ӧ180��
uint angle=0;//������ʼ�Ƕ�Ϊ0��
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
	TMOD |= 0x01;//��ʱ���㹤����ʽ1
	TL0 = 0xA3;
	TH0 = 0xFF;//��ʼֵ500us
	ET0 = 1;
	TF0 = 0;//���TF0��־
	TR0 = 1;//��ʱ����ʼ��ʱ
	
	//�����жϳ�ʼ��
	SCON=0x50;//�趨���ڹ�����ʽ0101 0000
	PCON=0x00;
	TMOD |=0x20;
	TL1=0xfd;//������9600
	TH1=0xfd;
	TR1=1;
	
	
	EA = 1;
	ES  = 1;       //���ж�
	
}

void main()
{
	Init();//��ʼ��
	while(1)
	{
		if(sw1==0)
		{
			//��angle=180ʱ��ֻ�ܰ�sw2����sw1��������
			if(angle<180)
			{
				for(i=0;i<=180;i++)
				{
					angle=i;
					delay(2);
				}
			}
			count=0;
			while(sw1==0);//ֱ�����ֲ�����ѭ��
		}
		if(sw2==0)
		{
			//��angle=0ʱ��ֻ�ܰ�sw1����sw2��������
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

void Timer() interrupt 1 //�жϳ���pwm�ź����
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
		ES=0;		//��ʱ�رմ����ж�
		//����յ�.
		RI = 0;      //�����־.
		New_rec = 1;
		UART_buff = SBUF;  //���յ�����Ϣ��SBUF�ŵ�UART_buff��
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
	{        //����ͱ�.
      TI = 0;      //�����־.
      Send_ed = 1;
    }
	ES=1;//���������ж�
}