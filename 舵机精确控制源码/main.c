#include<reg52.h>
#include<dingshiqi.h>

sbit k1 = P3^4;
sbit k2 = P3^5;//按钮串口
sbit pwm = P1^0;

unsigned int angle = 5;//调整初始角度
unsigned int cnt;


/*5对应0度 10对应45度 15对应90度 20对应135度 25对应180度*/

void delay(unsigned int ms)		//@11.0592MHz
{
	unsigned int i, j;
    for(i=0;i<ms;i++)
        for(j=0;j<112;j++);
}



void main()
{
    cnt=0;
    Timer0_Init();//定时器初始化
    while(1)
    {
        if(k1==0)
        {
            delay(10);//消抖
            if(k1==0)//再次确认
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

void Timer() interrupt 1 //中断程序pwm信号输出
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