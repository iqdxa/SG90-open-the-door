#include<reg52.h>
#include<dingshiqi.h>

void Timer0_Init()
{
	TMOD = 0x01;//��ʱ���㹤����ʽ1
    TL0 = 0xA3;
    TH0 = 0xFF;//��ʼֵ500us
    ET0 = 1;
    TF0 = 0;//���TF0��־
    TR0 = 1;//��ʱ����ʼ��ʱ
    EA = 1;//�ж��ܿ���
}