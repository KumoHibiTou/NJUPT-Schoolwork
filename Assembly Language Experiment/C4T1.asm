.486
CODE	  SEGMENT  USE16
          ASSUME  CS:CODE
BEG:      JMP     START
CCONPORT   EQU     213H             
CCONBIT1   EQU     00010110B
CCONBIT2   EQU     01010110B
CCONBIT3   EQU     10010110B
CDPORT1    EQU     210H               
CDPORT2    EQU     211H               
CDPORT3    EQU     212H               
CHDBIT1    EQU     00H
CHDBIT2    EQU     00H
CHDBIT3    EQU     00H
START:    
	NOP                        ;������ʱ
	MOV     DX,CCONPORT      ;д�������(������0)
	MOV     AL,CCONBIT1         
	OUT     DX,AL
	MOV     DX,CDPORT1       ;д���ֵ��������0��
	MOV     AL,CHDBIT1
	OUT     DX,AL
          
	MOV     DX,CCONPORT      ;д������֣�������1��
	MOV     AL,CCONBIT2
	OUT     DX,AL
	MOV     DX,CDPORT2       ;д���ֵ��������1��
	MOV     AL,CHDBIT2
	OUT     DX,AL 
	MOV     DX,CCONPORT     ;д������֣�������2��
	MOV     AL,CCONBIT3
	OUT     DX,AL
	MOV     DX,CDPORT3       ;д���ֵ��������2��
	MOV     AL,CHDBIT3
	OUT     DX,AL
WT:     
	NOP
	JMP WT
CODE ENDS
 END BEG
