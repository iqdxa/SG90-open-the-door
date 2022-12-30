#include<reg52.h>
#include<dingshiqi.h>

sbit k1 = P3^4;
sbit k2 = P3^5;//��ť����
sbit pwm = P1^0;

unsigned int angle = 5;//������ʼ�Ƕ�
unsigned int cnt;


/*5��Ӧ0�� 10��Ӧ45�� 15��Ӧ90�� 20��Ӧ135�� 25��Ӧ180��*/

void delay(unsigned int ms)		//@11.0592MHz
{
	unsigned int i, j;
    for(i=0;i<ms;i++)
        for(j=0;j<112;j++);
}



void main()
{
    cnt=0;
    Timer0_Init();//��ʱ����ʼ��
    while(1)
    {
        if(k1==0)
        {
            delay(10);//����
            if(k1==0)//�ٴ�ȷ��
            {
                angle++;
                cnt=0;
                if(angle==25)
                    angle=25;
                while(k1==0);
            }
        }
        if(k2==0)
        {
            delay(10);
            if(k2==0)
            {
                angle--;
                cnt=0;
                if(angle==5)
                    angle=5;
                while(k2==0);
            }
        }

    }
    while(1);
}

void Timer() interrupt 1 //�жϳ���pwm�ź����
{
    TL0 = 0xA3;
    TH0 = 0xFF;
    cnt++;
    if(cnt == 20)
    {
        cnt = 0;
    }
    if(cnt < angle)
    {
        pwm = 1;
    }
    else
    {
        pwm = 0;
    }
}