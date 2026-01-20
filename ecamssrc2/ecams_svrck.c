
/* Result Sets Interface */
#ifndef SQL_CRSR
#  define SQL_CRSR
  struct sql_cursor
  {
    unsigned int curocn;
    void *ptr1;
    void *ptr2;
    unsigned int magic;
  };
  typedef struct sql_cursor sql_cursor;
  typedef struct sql_cursor SQL_CURSOR;
#endif /* SQL_CRSR */

/* Thread Safety */
typedef void * sql_context;
typedef void * SQL_CONTEXT;

/* Object support */
struct sqltvn
{
  unsigned char *tvnvsn; 
  unsigned short tvnvsnl; 
  unsigned char *tvnnm;
  unsigned short tvnnml; 
  unsigned char *tvnsnm;
  unsigned short tvnsnml;
};
typedef struct sqltvn sqltvn;

struct sqladts
{
  unsigned int adtvsn; 
  unsigned short adtmode; 
  unsigned short adtnum;  
  sqltvn adttvn[1];       
};
typedef struct sqladts sqladts;

static struct sqladts sqladt = {
  1,1,0,
};

/* Binding to PL/SQL Records */
struct sqltdss
{
  unsigned int tdsvsn; 
  unsigned short tdsnum; 
  unsigned char *tdsval[1]; 
};
typedef struct sqltdss sqltdss;
static struct sqltdss sqltds =
{
  1,
  0,
};

/* File name & Package Name */
struct sqlcxp
{
  unsigned short fillen;
           char  filnam[15];
};
static struct sqlcxp sqlfpn =
{
    14,
    "ecams_svrck.pc"
};


static unsigned int sqlctx = 1139299;


static struct sqlexd {
   unsigned long  sqlvsn;
   unsigned int   arrsiz;
   unsigned int   iters;
   unsigned int   offset;
   unsigned short selerr;
   unsigned short sqlety;
   unsigned int   occurs;
            short *cud;
   unsigned char  *sqlest;
            char  *stmt;
   sqladts *sqladtp;
   sqltdss *sqltdsp;
   unsigned char  **sqphsv;
   unsigned long  *sqphsl;
            int   *sqphss;
            short **sqpind;
            int   *sqpins;
   unsigned long  *sqparm;
   unsigned long  **sqparc;
   unsigned short  *sqpadto;
   unsigned short  *sqptdso;
   unsigned int   sqlcmax;
   unsigned int   sqlcmin;
   unsigned int   sqlcincr;
   unsigned int   sqlctimeout;
   unsigned int   sqlcnowait;
            int   sqfoff;
   unsigned int   sqcmod;
   unsigned int   sqfmod;
   unsigned int   sqlpfmem;
   unsigned char  *sqhstv[7];
   unsigned long  sqhstl[7];
            int   sqhsts[7];
            short *sqindv[7];
            int   sqinds[7];
   unsigned long  sqharm[7];
   unsigned long  *sqharc[7];
   unsigned short  sqadto[7];
   unsigned short  sqtdso[7];
} sqlstm = {13,7};

/* SQLLIB Prototypes */
extern sqlcxt (/*_ void **, unsigned int *,
                   struct sqlexd *, struct sqlcxp * _*/);
extern sqlcx2t(/*_ void **, unsigned int *,
                   struct sqlexd *, struct sqlcxp * _*/);
extern sqlbuft(/*_ void **, char * _*/);
extern sqlgs2t(/*_ void **, char * _*/);
extern sqlorat(/*_ void **, unsigned int *, void * _*/);

/* Forms Interface */
static int IAPSUCC = 0;
static int IAPFAIL = 1403;
static int IAPFTL  = 535;
extern void sqliem(/*_ unsigned char *, signed int * _*/);

 static char *sq0003 = 
"select distinct A.CM_SYSCD ,A.CM_SVRCD ,B.CM_CODENAME ,A.CM_SVRIP ,NVL(A.CM\
_PORTNO,0)  from CMM0031 A ,CMM0020 B ,CMM0030 C where ((((((((A.CM_CLOSEDT i\
s null  and A.CM_SVRIP is  not null ) and A.CM_SVRSTOP='N') and SUBSTR(C.CM_S\
YSINFO,19,1)='0') and A.CM_SYSCD in (select CM_SYSCD  from CMM0030 where (CM_\
CLOSEDT is null  and CM_SYSCD=DECODE(:b0,'ALL',CM_SYSCD,:b0)))) and B.CM_MACO\
DE='SERVERCD') and B.CM_MICODE=A.CM_SVRCD) and A.CM_SYSCD=C.CM_SYSCD) and C.C\
M_CLOSEDT is null ) order by A.CM_SYSCD,A.CM_SVRCD,A.CM_SVRIP,NVL(A.CM_PORTNO\
,0)            ";

typedef struct { unsigned short len; unsigned char arr[1]; } VARCHAR;
typedef struct { unsigned short len; unsigned char arr[1]; } varchar;

/* CUD (Compilation Unit Data) Array */
static short sqlcud0[] =
{13,4274,846,0,0,
5,0,0,1,364,0,4,142,0,0,4,2,0,1,0,2,3,0,0,2,3,0,0,1,97,0,0,1,0,0,0,
36,0,0,2,125,0,4,164,0,0,3,2,0,1,0,2,3,0,0,1,97,0,0,1,0,0,0,
63,0,0,3,552,0,9,208,0,0,2,2,0,1,0,1,97,0,0,1,0,0,0,
86,0,0,3,0,0,13,210,0,0,5,0,0,1,0,2,97,0,0,2,97,0,0,2,97,0,0,2,97,0,0,2,3,0,0,
121,0,0,4,58,0,4,234,0,0,2,1,0,1,0,2,97,0,0,1,97,0,0,
144,0,0,5,142,0,5,327,0,0,7,7,0,1,0,1,97,0,0,1,97,0,0,1,97,0,0,1,97,0,0,1,97,0,
0,1,97,0,0,1,3,0,0,
187,0,0,6,0,0,29,337,0,0,0,0,0,1,0,
202,0,0,7,162,0,5,346,0,0,5,5,0,1,0,1,97,0,0,1,97,0,0,1,97,0,0,1,97,0,0,1,3,0,
0,
237,0,0,8,0,0,29,356,0,0,0,0,0,1,0,
252,0,0,3,0,0,15,358,0,0,0,0,0,1,0,
267,0,0,9,0,0,30,367,0,0,0,0,0,1,0,
};


/*-----------------------------------------------------------------
 ┌──────┬───────────────────────┐
 │ 프로그램명 │ ecams_svrck.pc                               │
 ├──────┼───────────────────────┤
 │ 기      능 │ 변경관리 대상서버시스템 Socket Process Check │
 ├──────┼───────────────────────┤
 │ 작  성  일 │ 2011. 07. 11                                 │
 ├──────┼───────────────────────┤
 │ 작  성  자 │ 최   병   남                                 │
 └──────┴───────────────────────┘
-----------------------------------------------------------------*/

#define	dfMain		1

/*---------------------------------------------------------------*/
/*     Header files                                              */
/*---------------------------------------------------------------*/
#include    <ecamsapi.h>


/* EXEC SQL INCLUDE "ecams_acct.h";
 */ 
/*-----------------------------------------------------------------
 ┌──────┬───────────────────────┐
 │ 프로그램명 │ ecams_acct.h                                 │
 ├──────┼───────────────────────┤
 │ 기      능 │ 신청자원 처리관련 HEADER FILE                │
 ├──────┼───────────────────────┤
 │ 작  성  일 │ 2007. 10. 24                                 │
 ├──────┼───────────────────────┤
 │ 작  성  자 │ 최   병   남                                 │
 └──────┴───────────────────────┘
-----------------------------------------------------------------*/

#include <stdio.h>
#include <unistd.h>

#define ECAMS_HOMEDIR 		   ""
 
/* EXEC SQL INCLUDE SQLCA;
 */ 
/*
 * $Header: sqlca.h 24-apr-2003.12:50:58 mkandarp Exp $ sqlca.h 
 */

/* Copyright (c) 1985, 2003, Oracle Corporation.  All rights reserved.  */
 
/*
NAME
  SQLCA : SQL Communications Area.
FUNCTION
  Contains no code. Oracle fills in the SQLCA with status info
  during the execution of a SQL stmt.
NOTES
  **************************************************************
  ***                                                        ***
  *** This file is SOSD.  Porters must change the data types ***
  *** appropriately on their platform.  See notes/pcport.doc ***
  *** for more information.                                  ***
  ***                                                        ***
  **************************************************************

  If the symbol SQLCA_STORAGE_CLASS is defined, then the SQLCA
  will be defined to have this storage class. For example:
 
    #define SQLCA_STORAGE_CLASS extern
 
  will define the SQLCA as an extern.
 
  If the symbol SQLCA_INIT is defined, then the SQLCA will be
  statically initialized. Although this is not necessary in order
  to use the SQLCA, it is a good pgming practice not to have
  unitialized variables. However, some C compilers/OS's don't
  allow automatic variables to be init'd in this manner. Therefore,
  if you are INCLUDE'ing the SQLCA in a place where it would be
  an automatic AND your C compiler/OS doesn't allow this style
  of initialization, then SQLCA_INIT should be left undefined --
  all others can define SQLCA_INIT if they wish.

  If the symbol SQLCA_NONE is defined, then the SQLCA variable will
  not be defined at all.  The symbol SQLCA_NONE should not be defined
  in source modules that have embedded SQL.  However, source modules
  that have no embedded SQL, but need to manipulate a sqlca struct
  passed in as a parameter, can set the SQLCA_NONE symbol to avoid
  creation of an extraneous sqlca variable.
 
MODIFIED
    lvbcheng   07/31/98 -  long to int
    jbasu      12/12/94 -  Bug 217878: note this is an SOSD file
    losborne   08/11/92 -  No sqlca var if SQLCA_NONE macro set 
  Clare      12/06/84 - Ch SQLCA to not be an extern.
  Clare      10/21/85 - Add initialization.
  Bradbury   01/05/86 - Only initialize when SQLCA_INIT set
  Clare      06/12/86 - Add SQLCA_STORAGE_CLASS option.
*/
 
#ifndef SQLCA
#define SQLCA 1
 
struct   sqlca
         {
         /* ub1 */ char    sqlcaid[8];
         /* b4  */ int     sqlabc;
         /* b4  */ int     sqlcode;
         struct
           {
           /* ub2 */ unsigned short sqlerrml;
           /* ub1 */ char           sqlerrmc[70];
           } sqlerrm;
         /* ub1 */ char    sqlerrp[8];
         /* b4  */ int     sqlerrd[6];
         /* ub1 */ char    sqlwarn[8];
         /* ub1 */ char    sqlext[8];
         };

#ifndef SQLCA_NONE 
#ifdef   SQLCA_STORAGE_CLASS
SQLCA_STORAGE_CLASS struct sqlca sqlca
#else
         struct sqlca sqlca
#endif
 
#ifdef  SQLCA_INIT
         = {
         {'S', 'Q', 'L', 'C', 'A', ' ', ' ', ' '},
         sizeof(struct sqlca),
         0,
         { 0, {0}},
         {'N', 'O', 'T', ' ', 'S', 'E', 'T', ' '},
         {0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0},
         {0, 0, 0, 0, 0, 0, 0, 0}
         }
#endif
         ;
#endif
 
#endif
 
/* end SQLCA */


/*---------------------------------------------------------------*/
/*		Structure for VARRAW                                     */
/*---------------------------------------------------------------*/
typedef struct
{
int  len;
unsigned char 	arr[1];
} my_raw;
/* EXEC SQL  TYPE my_raw IS LONG VARRAW(1000000000) REFERENCE; */ 


/*---------------------------------------------------------------*/
/*		전역변수 			                                     */
/*---------------------------------------------------------------*/
char	**gEnvp;                    	  /* 환경 변수           */
#ifndef	dfMain
	extern char	gszLogFile          [50]; /* Log File Name       */
	extern char	gszLogPath         [100]; /* Log File Path       */
	extern char	gszLogMsg          [512]; /* Log Message         */
	extern char	gszTempPath        [100]; /* Temp Direcotry      */
	extern char	gszBackPath        [256]; /* 소스백업 디렉토리   */
	extern char	szConnID           [100]; /* 개발CONNECT ID      */
#else
	char 	gszLogFile              [50]; /* Log File Name       */
	char 	gszLogPath             [100]; /* Log File Path       */
	char 	gszLogMsg              [512]; /* Log Message         */
	char 	gszTempPath            [100]; /* Temp Direcotry      */
	char	gszBackPath            [256]; /* 소스백업 디렉토리   */
	char	szConnID               [100]; /* 개발CONNECT ID      */
#endif

/*---------------------------------------------------------------*/
/*                E N D   O F   F I L E                          */
/*---------------------------------------------------------------*/



/*---------------------------------------------------------------*/
/*    User Work 변수                                             */
/*---------------------------------------------------------------*/
int      cSockId    ;                 /* Connect Socket ID       */
int  	 gRcvFd     ;                 /* 수신용 File Pointer     */
int  	 gSndFd     ;                 /* 송신용 File Pointer     */
int  	 gProcStep  ;                 /* 송신/수신 상태          */
int  	 gProcWork  ;                 /* 송신/수신 여부          */
int  	 gFileOffset;                 /* 파일 송신 OffSet        */
char     gJobGub    [2]             ; /* 처리구분                */
char     gCommand   [dfeCAMSBufSize]; /* 처리명령문              */
char     gLocal     [dfeCAMSBufSize]; /* Local File Name         */
char     gLocal1    [dfeCAMSBufSize]; /* Local File Name         */
char     gRemote    [dfeCAMSBufSize]; /* Remote File Name        */
int      gFileSize             ;      /* File Size               */
char     gFileDate         [20];      /* File Date               */
char     gRstIPAddr        [20];
char     systime  [16], sysdate [16]; /* 시스템일자, 시간        */
char     filename         [200];      /* File Name               */
char     SysCmd           [200];      /* 시스템 명령문           */
char     indat            [256];      /* Process Name Work       */
int      nret                  ;
int      nret1                 ;
char     sBackSorc1       [200];      /* BackUp File Name        */
int      i                     ;      /* Loop Count Work         */


time_t       time_clock;

/*---------------------------------------------------------------*/
/*   eCAMS  LOGGING 관련  CONSTANT                               */
/*---------------------------------------------------------------*/
FILE   *fd;                           /* Logging File Pointer    */
struct stat  f_st;                    /* Logging File Struct     */
struct tm   *loc;                     /* File 정보 Read Pointer  */

char   workbuf [dfeCAMSBufSize];      /* Logging Buffer          */
WorkStatTag  *stat_t;                 /* Logging Buffer Pointer  */


/*---------------------------------------------------------------*/
/*   SOCKET 통신관련  CONSTANT                                   */
/*---------------------------------------------------------------*/
ulong  ul_servip;                     /* Server IP의 Convert IP  */
int    retval;                        /* 처리결과                */
FILE   *ScrPtr;                       /* LocalFile작성용 Pointer */
char   **gEnvp;                       /* Parameter죵료용 Pointer */

extern    int    errno;               /* 시스템 오류코드         */



/*****************************************************************/
/*                                                               */
/*       서버 Agent 상태 체크 처리 M A I N                       */
/*                                                               */
/*****************************************************************/
main 	(int argc, char **argv, char **envp)
{
char	szSysCD                [dfSysCD]; /* 시스템 ID           */
char	ssyscd                 [dfSysCD]; /* 시스템 ID           */
char	sSvrcd                 [dfSvrCD]; /* 서버구분            */
char	ServerIP               [dfSvrIP]; /* SERVER IPADDRESS    */
char	sRstCond             [dfRstCode]; /* SERVER Check 결과   */
char	szSysMsg                  [30+1]; /* 시스템설명          */
char	szCodeName                [20+1]; /* 코드명칭            */
char	szSvrOSNM                  [256]; /* 서버 OS NAME        */
char	szSvrNM                    [256]; /* 서버 OS NAME        */
char	szSvrOSVer                 [256]; /* 서버 OS NAME        */
char	szSvrOSDir                [1024]; /* Agent 설치 디렉토리 */
char	szSvrUser                 [1024]; /* Agent 기동 계정     */
int 	SvrPort                         ; /* 사용 PROT           */
int		nTotCnt                         ; /* 대상서버 대수       */
int		nOKCnt                          ; /* 정상서버 대수       */
int 	nErrCnt                         ; /* 오류서버 대수       */
int		nSize1                          ;
int		nSize2                          ;
int		nSize3                          ;
int 	nRet                            ;
FILE	*SRCPTR                         ;

    gEnvp = envp;

    /*-----------------------------------------------------------*/
    /*   동시 처리 여부 체크하여 동시처리 방지                   */
    /*-----------------------------------------------------------*/
	sprintf(SysCmd, "procck %s", argv[0]);
	retval = system(SysCmd)/256;
	if (retval > 1)	exit (1);

	if (argc > 1) {
		sprintf(szSysCD, "%s", argv[1]);
	}
	else {
		sprintf(szSysCD, "%s", "ALL");
	}
	
	
    /*-----------------------------------------------------------*/
    /*   eCAMS  DB Server  Connect                               */
    /*-----------------------------------------------------------*/
	if (ConnectDB("ECAMS") == FALSE)
		exit(1);


    nTotCnt = 0;
    nOKCnt  = 0;
    nErrCnt = 0;

    nSize1 = 0;
    nSize2 = 0;
    nSize3 = 0;

    DefineSignal ();

    /*-----------------------------------------------------------*/
    /*   조회항목별 최대길이 계산                                */
    /*-----------------------------------------------------------*/
	/* EXEC SQL
		SELECT  MAX(LENGTHB(b.CM_CodeName))
		       ,MAX(LENGTHB(a.CM_SvrIP   ))
		  INTO  :nSize1
		       ,:nSize2
          FROM  CMM0031 a
              , CMM0020 b
         WHERE  a.CM_CLOSEDT IS NULL
           AND  a.CM_SVRIP   IS NOT NULL
           AND  a.CM_SVRSTOP = 'N'
           AND  a.CM_Syscd IN (SELECT  CM_Syscd
                                 FROM  CMM0030
                                WHERE  CM_Closedt IS NULL
                                  AND  CM_SYSCD = DECODE(:szSysCD, 'ALL', CM_SYSCD, :szSysCD)
                              )
           AND  b.CM_MaCode = 'SERVERCD'
           AND  b.CM_MiCode = a.CM_SvrCD; */ 

{
 struct sqlexd sqlstm;
 sqlstm.sqlvsn = 13;
 sqlstm.arrsiz = 4;
 sqlstm.sqladtp = &sqladt;
 sqlstm.sqltdsp = &sqltds;
 sqlstm.stmt = "select max(LENGTHB(b.CM_CodeName)) ,max(LENGTHB(a.CM_SvrIP)\
) into :b0,:b1  from CMM0031 a ,CMM0020 b where (((((a.CM_CLOSEDT is null  an\
d a.CM_SVRIP is  not null ) and a.CM_SVRSTOP='N') and a.CM_Syscd in (select C\
M_Syscd  from CMM0030 where (CM_Closedt is null  and CM_SYSCD=DECODE(:b2,'ALL\
',CM_SYSCD,:b2)))) and b.CM_MaCode='SERVERCD') and b.CM_MiCode=a.CM_SvrCD)";
 sqlstm.iters = (unsigned int  )1;
 sqlstm.offset = (unsigned int  )5;
 sqlstm.selerr = (unsigned short)1;
 sqlstm.sqlpfmem = (unsigned int  )0;
 sqlstm.cud = sqlcud0;
 sqlstm.sqlest = (unsigned char  *)&sqlca;
 sqlstm.sqlety = (unsigned short)4352;
 sqlstm.occurs = (unsigned int  )0;
 sqlstm.sqhstv[0] = (unsigned char  *)&nSize1;
 sqlstm.sqhstl[0] = (unsigned long )sizeof(int);
 sqlstm.sqhsts[0] = (         int  )0;
 sqlstm.sqindv[0] = (         short *)0;
 sqlstm.sqinds[0] = (         int  )0;
 sqlstm.sqharm[0] = (unsigned long )0;
 sqlstm.sqadto[0] = (unsigned short )0;
 sqlstm.sqtdso[0] = (unsigned short )0;
 sqlstm.sqhstv[1] = (unsigned char  *)&nSize2;
 sqlstm.sqhstl[1] = (unsigned long )sizeof(int);
 sqlstm.sqhsts[1] = (         int  )0;
 sqlstm.sqindv[1] = (         short *)0;
 sqlstm.sqinds[1] = (         int  )0;
 sqlstm.sqharm[1] = (unsigned long )0;
 sqlstm.sqadto[1] = (unsigned short )0;
 sqlstm.sqtdso[1] = (unsigned short )0;
 sqlstm.sqhstv[2] = (unsigned char  *)szSysCD;
 sqlstm.sqhstl[2] = (unsigned long )6;
 sqlstm.sqhsts[2] = (         int  )0;
 sqlstm.sqindv[2] = (         short *)0;
 sqlstm.sqinds[2] = (         int  )0;
 sqlstm.sqharm[2] = (unsigned long )0;
 sqlstm.sqadto[2] = (unsigned short )0;
 sqlstm.sqtdso[2] = (unsigned short )0;
 sqlstm.sqhstv[3] = (unsigned char  *)szSysCD;
 sqlstm.sqhstl[3] = (unsigned long )6;
 sqlstm.sqhsts[3] = (         int  )0;
 sqlstm.sqindv[3] = (         short *)0;
 sqlstm.sqinds[3] = (         int  )0;
 sqlstm.sqharm[3] = (unsigned long )0;
 sqlstm.sqadto[3] = (unsigned short )0;
 sqlstm.sqtdso[3] = (unsigned short )0;
 sqlstm.sqphsv = sqlstm.sqhstv;
 sqlstm.sqphsl = sqlstm.sqhstl;
 sqlstm.sqphss = sqlstm.sqhsts;
 sqlstm.sqpind = sqlstm.sqindv;
 sqlstm.sqpins = sqlstm.sqinds;
 sqlstm.sqparm = sqlstm.sqharm;
 sqlstm.sqparc = sqlstm.sqharc;
 sqlstm.sqpadto = sqlstm.sqadto;
 sqlstm.sqptdso = sqlstm.sqtdso;
 sqlcxt((void **)0, &sqlctx, &sqlstm, &sqlfpn);
}




    /*-----------------------------------------------------------*/
    /*   시스템명칭 최대길이 계산                                */
    /*-----------------------------------------------------------*/
	/* EXEC SQL
		SELECT  MAX(LENGTHB(CM_SYSMSG))
		  INTO  :nSize3
		  FROM  CMM0030
		 WHERE  CM_Closedt IS NULL
		   AND  CM_SYSCD = DECODE(:szSysCD, 'ALL', CM_SYSCD, :szSysCD); */ 

{
 struct sqlexd sqlstm;
 sqlstm.sqlvsn = 13;
 sqlstm.arrsiz = 4;
 sqlstm.sqladtp = &sqladt;
 sqlstm.sqltdsp = &sqltds;
 sqlstm.stmt = "select max(LENGTHB(CM_SYSMSG)) into :b0  from CMM0030 where\
 (CM_Closedt is null  and CM_SYSCD=DECODE(:b1,'ALL',CM_SYSCD,:b1))";
 sqlstm.iters = (unsigned int  )1;
 sqlstm.offset = (unsigned int  )36;
 sqlstm.selerr = (unsigned short)1;
 sqlstm.sqlpfmem = (unsigned int  )0;
 sqlstm.cud = sqlcud0;
 sqlstm.sqlest = (unsigned char  *)&sqlca;
 sqlstm.sqlety = (unsigned short)4352;
 sqlstm.occurs = (unsigned int  )0;
 sqlstm.sqhstv[0] = (unsigned char  *)&nSize3;
 sqlstm.sqhstl[0] = (unsigned long )sizeof(int);
 sqlstm.sqhsts[0] = (         int  )0;
 sqlstm.sqindv[0] = (         short *)0;
 sqlstm.sqinds[0] = (         int  )0;
 sqlstm.sqharm[0] = (unsigned long )0;
 sqlstm.sqadto[0] = (unsigned short )0;
 sqlstm.sqtdso[0] = (unsigned short )0;
 sqlstm.sqhstv[1] = (unsigned char  *)szSysCD;
 sqlstm.sqhstl[1] = (unsigned long )6;
 sqlstm.sqhsts[1] = (         int  )0;
 sqlstm.sqindv[1] = (         short *)0;
 sqlstm.sqinds[1] = (         int  )0;
 sqlstm.sqharm[1] = (unsigned long )0;
 sqlstm.sqadto[1] = (unsigned short )0;
 sqlstm.sqtdso[1] = (unsigned short )0;
 sqlstm.sqhstv[2] = (unsigned char  *)szSysCD;
 sqlstm.sqhstl[2] = (unsigned long )6;
 sqlstm.sqhsts[2] = (         int  )0;
 sqlstm.sqindv[2] = (         short *)0;
 sqlstm.sqinds[2] = (         int  )0;
 sqlstm.sqharm[2] = (unsigned long )0;
 sqlstm.sqadto[2] = (unsigned short )0;
 sqlstm.sqtdso[2] = (unsigned short )0;
 sqlstm.sqphsv = sqlstm.sqhstv;
 sqlstm.sqphsl = sqlstm.sqhstl;
 sqlstm.sqphss = sqlstm.sqhsts;
 sqlstm.sqpind = sqlstm.sqindv;
 sqlstm.sqpins = sqlstm.sqinds;
 sqlstm.sqparm = sqlstm.sqharm;
 sqlstm.sqparc = sqlstm.sqharc;
 sqlstm.sqpadto = sqlstm.sqadto;
 sqlstm.sqptdso = sqlstm.sqtdso;
 sqlcxt((void **)0, &sqlctx, &sqlstm, &sqlfpn);
}




    /*-----------------------------------------------------------*/
    /*   서버 Agent 체크 대상 조회                               */
    /*-----------------------------------------------------------*/
    /* EXEC SQL DECLARE SYSInfo CURSOR FOR
         SELECT  DISTINCT
                 A.CM_SYSCD
               , A.CM_SVRCD
               , B.CM_CODENAME
               , A.CM_SVRIP
               , NVL(A.CM_PORTNO, 0)
           FROM  CMM0031 A
               , CMM0020 B
               , CMM0030 C
          WHERE  A.CM_CLOSEDT IS NULL
            AND  A.CM_SVRIP   IS NOT NULL
            AND  A.CM_SVRSTOP = 'N'
			AND  SUBSTR(C.CM_SYSINFO,19,1) = '0'
            AND  A.CM_SYSCD IN (SELECT  CM_SYSCD
                                  FROM  CMM0030
                                 WHERE  CM_CLOSEDT IS NULL
                                   AND  CM_SYSCD = DECODE(:szSysCD, 'ALL', CM_SYSCD, :szSysCD)
                               )
            AND  B.CM_MACODE = 'SERVERCD'
            AND  B.CM_MICODE = A.CM_SVRCD
            AND  A.CM_SYSCD = C.CM_SYSCD
            AND  C.CM_CLOSEDT IS NULL 
          ORDER  BY  A.CM_SYSCD, A.CM_SVRCD, A.CM_SVRIP, NVL(A.CM_PORTNO, 0); */ 


	system ("clear");

	fprintf(stdout, "\n");
	fprintf(stdout, " \033[5m┌───────────────────────────────────────────┐\033[0m\n");
	fprintf(stdout, " \033[5m│              형상관리 적용대상 서버 CHECK (AGENT CHECK : ecams_svr)                  │\033[0m\n");
	fprintf(stdout, " \033[5m└───────────────────────────────────────────┘\033[0m\n");
	fprintf(stdout, "\n");

	/* EXEC SQL open SYSInfo; */ 

{
 struct sqlexd sqlstm;
 sqlstm.sqlvsn = 13;
 sqlstm.arrsiz = 4;
 sqlstm.sqladtp = &sqladt;
 sqlstm.sqltdsp = &sqltds;
 sqlstm.stmt = sq0003;
 sqlstm.iters = (unsigned int  )1;
 sqlstm.offset = (unsigned int  )63;
 sqlstm.selerr = (unsigned short)1;
 sqlstm.sqlpfmem = (unsigned int  )0;
 sqlstm.cud = sqlcud0;
 sqlstm.sqlest = (unsigned char  *)&sqlca;
 sqlstm.sqlety = (unsigned short)4352;
 sqlstm.occurs = (unsigned int  )0;
 sqlstm.sqcmod = (unsigned int )0;
 sqlstm.sqhstv[0] = (unsigned char  *)szSysCD;
 sqlstm.sqhstl[0] = (unsigned long )6;
 sqlstm.sqhsts[0] = (         int  )0;
 sqlstm.sqindv[0] = (         short *)0;
 sqlstm.sqinds[0] = (         int  )0;
 sqlstm.sqharm[0] = (unsigned long )0;
 sqlstm.sqadto[0] = (unsigned short )0;
 sqlstm.sqtdso[0] = (unsigned short )0;
 sqlstm.sqhstv[1] = (unsigned char  *)szSysCD;
 sqlstm.sqhstl[1] = (unsigned long )6;
 sqlstm.sqhsts[1] = (         int  )0;
 sqlstm.sqindv[1] = (         short *)0;
 sqlstm.sqinds[1] = (         int  )0;
 sqlstm.sqharm[1] = (unsigned long )0;
 sqlstm.sqadto[1] = (unsigned short )0;
 sqlstm.sqtdso[1] = (unsigned short )0;
 sqlstm.sqphsv = sqlstm.sqhstv;
 sqlstm.sqphsl = sqlstm.sqhstl;
 sqlstm.sqphss = sqlstm.sqhsts;
 sqlstm.sqpind = sqlstm.sqindv;
 sqlstm.sqpins = sqlstm.sqinds;
 sqlstm.sqparm = sqlstm.sqharm;
 sqlstm.sqparc = sqlstm.sqharc;
 sqlstm.sqpadto = sqlstm.sqadto;
 sqlstm.sqptdso = sqlstm.sqtdso;
 sqlcxt((void **)0, &sqlctx, &sqlstm, &sqlfpn);
}


	while (1) {
		/* EXEC SQL fetch SYSInfo
			INTO  :ssyscd
				, :sSvrcd
				, :szCodeName
				, :ServerIP
				, :SvrPort; */ 

{
  struct sqlexd sqlstm;
  sqlstm.sqlvsn = 13;
  sqlstm.arrsiz = 5;
  sqlstm.sqladtp = &sqladt;
  sqlstm.sqltdsp = &sqltds;
  sqlstm.iters = (unsigned int  )1;
  sqlstm.offset = (unsigned int  )86;
  sqlstm.selerr = (unsigned short)1;
  sqlstm.sqlpfmem = (unsigned int  )0;
  sqlstm.cud = sqlcud0;
  sqlstm.sqlest = (unsigned char  *)&sqlca;
  sqlstm.sqlety = (unsigned short)4352;
  sqlstm.occurs = (unsigned int  )0;
  sqlstm.sqfoff = (         int )0;
  sqlstm.sqfmod = (unsigned int )2;
  sqlstm.sqhstv[0] = (unsigned char  *)ssyscd;
  sqlstm.sqhstl[0] = (unsigned long )6;
  sqlstm.sqhsts[0] = (         int  )0;
  sqlstm.sqindv[0] = (         short *)0;
  sqlstm.sqinds[0] = (         int  )0;
  sqlstm.sqharm[0] = (unsigned long )0;
  sqlstm.sqadto[0] = (unsigned short )0;
  sqlstm.sqtdso[0] = (unsigned short )0;
  sqlstm.sqhstv[1] = (unsigned char  *)sSvrcd;
  sqlstm.sqhstl[1] = (unsigned long )3;
  sqlstm.sqhsts[1] = (         int  )0;
  sqlstm.sqindv[1] = (         short *)0;
  sqlstm.sqinds[1] = (         int  )0;
  sqlstm.sqharm[1] = (unsigned long )0;
  sqlstm.sqadto[1] = (unsigned short )0;
  sqlstm.sqtdso[1] = (unsigned short )0;
  sqlstm.sqhstv[2] = (unsigned char  *)szCodeName;
  sqlstm.sqhstl[2] = (unsigned long )21;
  sqlstm.sqhsts[2] = (         int  )0;
  sqlstm.sqindv[2] = (         short *)0;
  sqlstm.sqinds[2] = (         int  )0;
  sqlstm.sqharm[2] = (unsigned long )0;
  sqlstm.sqadto[2] = (unsigned short )0;
  sqlstm.sqtdso[2] = (unsigned short )0;
  sqlstm.sqhstv[3] = (unsigned char  *)ServerIP;
  sqlstm.sqhstl[3] = (unsigned long )21;
  sqlstm.sqhsts[3] = (         int  )0;
  sqlstm.sqindv[3] = (         short *)0;
  sqlstm.sqinds[3] = (         int  )0;
  sqlstm.sqharm[3] = (unsigned long )0;
  sqlstm.sqadto[3] = (unsigned short )0;
  sqlstm.sqtdso[3] = (unsigned short )0;
  sqlstm.sqhstv[4] = (unsigned char  *)&SvrPort;
  sqlstm.sqhstl[4] = (unsigned long )sizeof(int);
  sqlstm.sqhsts[4] = (         int  )0;
  sqlstm.sqindv[4] = (         short *)0;
  sqlstm.sqinds[4] = (         int  )0;
  sqlstm.sqharm[4] = (unsigned long )0;
  sqlstm.sqadto[4] = (unsigned short )0;
  sqlstm.sqtdso[4] = (unsigned short )0;
  sqlstm.sqphsv = sqlstm.sqhstv;
  sqlstm.sqphsl = sqlstm.sqhstl;
  sqlstm.sqphss = sqlstm.sqhsts;
  sqlstm.sqpind = sqlstm.sqindv;
  sqlstm.sqpins = sqlstm.sqinds;
  sqlstm.sqparm = sqlstm.sqharm;
  sqlstm.sqparc = sqlstm.sqharc;
  sqlstm.sqpadto = sqlstm.sqadto;
  sqlstm.sqptdso = sqlstm.sqtdso;
  sqlcxt((void **)0, &sqlctx, &sqlstm, &sqlfpn);
}



		NotUseDataTruncate(ssyscd  , 0x20);
		NotUseDataTruncate(sSvrcd  , 0x20);
		NotUseDataTruncate(ServerIP, 0x20);

		if (sqlca.sqlcode == 1403) break;

		if (sqlca.sqlcode != 0) {
			fprintf(stderr, "SQLCODE ERROR = [%d]\n", sqlca.sqlcode);
			break;
		}
			
		memset(szSysMsg, 0x20, sizeof(szSysMsg));
		szCodeName[nSize2 + 1] = '\0';

	    /*-------------------------------------------------------*/
	    /*   시스템 명칭 조회                                    */
	    /*-------------------------------------------------------*/
		/* EXEC SQL
			SELECT  CM_SYSMSG
			  INTO  :szSysMsg
			  FROM  CMM0030
			 WHERE  CM_SYSCD = :ssyscd; */ 

{
  struct sqlexd sqlstm;
  sqlstm.sqlvsn = 13;
  sqlstm.arrsiz = 5;
  sqlstm.sqladtp = &sqladt;
  sqlstm.sqltdsp = &sqltds;
  sqlstm.stmt = "select CM_SYSMSG into :b0  from CMM0030 where CM_SYSCD=:b1";
  sqlstm.iters = (unsigned int  )1;
  sqlstm.offset = (unsigned int  )121;
  sqlstm.selerr = (unsigned short)1;
  sqlstm.sqlpfmem = (unsigned int  )0;
  sqlstm.cud = sqlcud0;
  sqlstm.sqlest = (unsigned char  *)&sqlca;
  sqlstm.sqlety = (unsigned short)4352;
  sqlstm.occurs = (unsigned int  )0;
  sqlstm.sqhstv[0] = (unsigned char  *)szSysMsg;
  sqlstm.sqhstl[0] = (unsigned long )31;
  sqlstm.sqhsts[0] = (         int  )0;
  sqlstm.sqindv[0] = (         short *)0;
  sqlstm.sqinds[0] = (         int  )0;
  sqlstm.sqharm[0] = (unsigned long )0;
  sqlstm.sqadto[0] = (unsigned short )0;
  sqlstm.sqtdso[0] = (unsigned short )0;
  sqlstm.sqhstv[1] = (unsigned char  *)ssyscd;
  sqlstm.sqhstl[1] = (unsigned long )6;
  sqlstm.sqhsts[1] = (         int  )0;
  sqlstm.sqindv[1] = (         short *)0;
  sqlstm.sqinds[1] = (         int  )0;
  sqlstm.sqharm[1] = (unsigned long )0;
  sqlstm.sqadto[1] = (unsigned short )0;
  sqlstm.sqtdso[1] = (unsigned short )0;
  sqlstm.sqphsv = sqlstm.sqhstv;
  sqlstm.sqphsl = sqlstm.sqhstl;
  sqlstm.sqphss = sqlstm.sqhsts;
  sqlstm.sqpind = sqlstm.sqindv;
  sqlstm.sqpins = sqlstm.sqinds;
  sqlstm.sqparm = sqlstm.sqharm;
  sqlstm.sqparc = sqlstm.sqharc;
  sqlstm.sqpadto = sqlstm.sqadto;
  sqlstm.sqptdso = sqlstm.sqtdso;
  sqlcxt((void **)0, &sqlctx, &sqlstm, &sqlfpn);
}



		if (SvrPort == 0)
			SvrPort = dfeCAMSFepPort;

		nTotCnt++;
		szSysMsg[nSize3 + 1] = '\0';

		ul_servip = CvtIPchr2ulong (ServerIP);

		memset(sRstCond, 0x00, sizeof(sRstCond));
		cSockId = ConnectClient2Server (SvrPort, ul_servip);
		if (cSockId <= 0) {
			nErrCnt++;
			fprintf(stdout, "  [%s %s] [%s] [%-16s] [%6d] Error[C]\n", ssyscd, szSysMsg, szCodeName, ServerIP, SvrPort);
			sprintf(sRstCond, "ER");
		}
		else {
			nOKCnt++;

			memset(szSvrNM   , 0x00, sizeof(szSvrNM   ));
			memset(szSvrUser , 0x00, sizeof(szSvrUser ));
			memset(szSvrOSDir, 0x00, sizeof(szSvrOSDir));
			
			if (ServerCmd(ServerIP, SvrPort, "S", "", "", "hostname > SVROS.TXT", 0) == 0) {
				if (ServerCmd(ServerIP, SvrPort, "G", "SVROS.txt", "SVROS.TXT", "", 0) == 0) {			
					memset(szSvrOSNM , 0x00, sizeof(szSvrOSNM ));
					memset(szSvrNM   , 0x00, sizeof(szSvrNM   ));
					memset(szSvrOSVer, 0x00, sizeof(szSvrOSVer));
				    if ((SRCPTR = fopen("SVROS.txt", "r")) != (FILE *) NULL) {
					    fgets(indat, 256, SRCPTR);
						fclose (SRCPTR);
						
						sprintf(indat, "%s", rep_char(indat, "\r", ""));
						sprintf(indat, "%s", rep_char(indat, "\n", ""));
						sprintf(indat, "%s", trunc_char(indat));
									
						sprintf(szSvrNM, "%s", indat);
					}
				}
			}
			ServerCmd(ServerIP, SvrPort, "S", "", "", "\\rm SVROS.TXT", 0);
			remove("SVROS.txt");

			if (ServerCmd(ServerIP, SvrPort, "S", "", "", "whoami > SVRPWD.TXT", 0) == 0) {
				if (ServerCmd(ServerIP, SvrPort, "G", "SVRPWD.txt", "SVRPWD.TXT", "", 0) == 0) {
					memset(szSvrOSDir, 0x00, sizeof(szSvrOSDir));
				    if ((SRCPTR = fopen("SVRPWD.txt", "r")) != (FILE *) NULL) {
					    fgets(indat, 256, SRCPTR);
						fclose (SRCPTR);
						
						sprintf(indat, "%s", rep_char(indat, "\r", ""));
						sprintf(indat, "%s", rep_char(indat, "\n", ""));
						sprintf(indat, "%s", trunc_char(indat));
						strcpy(szSvrUser, indat);
						
				    }
				}
			}
			ServerCmd(ServerIP, SvrPort, "S", "", "", "\\rm SVRPWD.TXT", 0);
			remove("SVRPWD.txt");
			

			if (ServerCmd(ServerIP, SvrPort, "S", "", "", "pwd > SVRPWD.TXT", 0) == 0) {
				if (ServerCmd(ServerIP, SvrPort, "G", "SVRPWD.txt", "SVRPWD.TXT", "", 0) == 0) {
					memset(szSvrOSDir, 0x00, sizeof(szSvrOSDir));
				    if ((SRCPTR = fopen("SVRPWD.txt", "r")) != (FILE *) NULL) {
					    fgets(indat, 256, SRCPTR);
						fclose (SRCPTR);
						
						sprintf(indat, "%s", rep_char(indat, "\r", ""));
						sprintf(indat, "%s", rep_char(indat, "\n", ""));
						sprintf(indat, "%s", trunc_char(indat));
						strcpy(szSvrOSDir, indat);
						
				    }
				}
			}
			ServerCmd(ServerIP, SvrPort, "S", "", "", "\\rm SVRPWD.TXT", 0);
			remove("SVRPWD.txt");
			
			fprintf(stdout, "  [%s %s] [%s] [%-16s] [%6d] OK   [%-15s] [%-20s] [%s]\n", ssyscd, szSysMsg, szCodeName, ServerIP, SvrPort, szSvrNM, szSvrUser, szSvrOSDir);

			/*---------------------------------------------------*/
			/*	서버 디렉토리 정보 변경                          */
			/*---------------------------------------------------*/
			if (strlen(szSvrNM   ) > 0 &&
				strlen(szSvrUser ) > 0 &&
				strlen(szSvrOSDir) > 0 ) {
				/* EXEC SQL 
					UPDATE  CMM0031
					   SET  CM_SVRNAME = :szSvrNM
					      , CM_SVRUSR  = :szSvrUser
					      , CM_DIR     = :szSvrOSDir
					 WHERE  CM_SYSCD   = :ssyscd
					   AND  CM_SVRCD   = :sSvrcd
					   AND  CM_SVRIP   = TRIM(:ServerIP)
					   AND  CM_PORTNO  = :SvrPort; */ 

{
    struct sqlexd sqlstm;
    sqlstm.sqlvsn = 13;
    sqlstm.arrsiz = 7;
    sqlstm.sqladtp = &sqladt;
    sqlstm.sqltdsp = &sqltds;
    sqlstm.stmt = "update CMM0031  set CM_SVRNAME=:b0,CM_SVRUSR=:b1,CM_DIR=\
:b2 where (((CM_SYSCD=:b3 and CM_SVRCD=:b4) and CM_SVRIP=trim(:b5)) and CM_PO\
RTNO=:b6)";
    sqlstm.iters = (unsigned int  )1;
    sqlstm.offset = (unsigned int  )144;
    sqlstm.cud = sqlcud0;
    sqlstm.sqlest = (unsigned char  *)&sqlca;
    sqlstm.sqlety = (unsigned short)4352;
    sqlstm.occurs = (unsigned int  )0;
    sqlstm.sqhstv[0] = (unsigned char  *)szSvrNM;
    sqlstm.sqhstl[0] = (unsigned long )256;
    sqlstm.sqhsts[0] = (         int  )0;
    sqlstm.sqindv[0] = (         short *)0;
    sqlstm.sqinds[0] = (         int  )0;
    sqlstm.sqharm[0] = (unsigned long )0;
    sqlstm.sqadto[0] = (unsigned short )0;
    sqlstm.sqtdso[0] = (unsigned short )0;
    sqlstm.sqhstv[1] = (unsigned char  *)szSvrUser;
    sqlstm.sqhstl[1] = (unsigned long )1024;
    sqlstm.sqhsts[1] = (         int  )0;
    sqlstm.sqindv[1] = (         short *)0;
    sqlstm.sqinds[1] = (         int  )0;
    sqlstm.sqharm[1] = (unsigned long )0;
    sqlstm.sqadto[1] = (unsigned short )0;
    sqlstm.sqtdso[1] = (unsigned short )0;
    sqlstm.sqhstv[2] = (unsigned char  *)szSvrOSDir;
    sqlstm.sqhstl[2] = (unsigned long )1024;
    sqlstm.sqhsts[2] = (         int  )0;
    sqlstm.sqindv[2] = (         short *)0;
    sqlstm.sqinds[2] = (         int  )0;
    sqlstm.sqharm[2] = (unsigned long )0;
    sqlstm.sqadto[2] = (unsigned short )0;
    sqlstm.sqtdso[2] = (unsigned short )0;
    sqlstm.sqhstv[3] = (unsigned char  *)ssyscd;
    sqlstm.sqhstl[3] = (unsigned long )6;
    sqlstm.sqhsts[3] = (         int  )0;
    sqlstm.sqindv[3] = (         short *)0;
    sqlstm.sqinds[3] = (         int  )0;
    sqlstm.sqharm[3] = (unsigned long )0;
    sqlstm.sqadto[3] = (unsigned short )0;
    sqlstm.sqtdso[3] = (unsigned short )0;
    sqlstm.sqhstv[4] = (unsigned char  *)sSvrcd;
    sqlstm.sqhstl[4] = (unsigned long )3;
    sqlstm.sqhsts[4] = (         int  )0;
    sqlstm.sqindv[4] = (         short *)0;
    sqlstm.sqinds[4] = (         int  )0;
    sqlstm.sqharm[4] = (unsigned long )0;
    sqlstm.sqadto[4] = (unsigned short )0;
    sqlstm.sqtdso[4] = (unsigned short )0;
    sqlstm.sqhstv[5] = (unsigned char  *)ServerIP;
    sqlstm.sqhstl[5] = (unsigned long )21;
    sqlstm.sqhsts[5] = (         int  )0;
    sqlstm.sqindv[5] = (         short *)0;
    sqlstm.sqinds[5] = (         int  )0;
    sqlstm.sqharm[5] = (unsigned long )0;
    sqlstm.sqadto[5] = (unsigned short )0;
    sqlstm.sqtdso[5] = (unsigned short )0;
    sqlstm.sqhstv[6] = (unsigned char  *)&SvrPort;
    sqlstm.sqhstl[6] = (unsigned long )sizeof(int);
    sqlstm.sqhsts[6] = (         int  )0;
    sqlstm.sqindv[6] = (         short *)0;
    sqlstm.sqinds[6] = (         int  )0;
    sqlstm.sqharm[6] = (unsigned long )0;
    sqlstm.sqadto[6] = (unsigned short )0;
    sqlstm.sqtdso[6] = (unsigned short )0;
    sqlstm.sqphsv = sqlstm.sqhstv;
    sqlstm.sqphsl = sqlstm.sqhstl;
    sqlstm.sqphss = sqlstm.sqhsts;
    sqlstm.sqpind = sqlstm.sqindv;
    sqlstm.sqpins = sqlstm.sqinds;
    sqlstm.sqparm = sqlstm.sqharm;
    sqlstm.sqparc = sqlstm.sqharc;
    sqlstm.sqpadto = sqlstm.sqadto;
    sqlstm.sqptdso = sqlstm.sqtdso;
    sqlcxt((void **)0, &sqlctx, &sqlstm, &sqlfpn);
}


				
				/* EXEC SQL COMMIT; */ 

{
    struct sqlexd sqlstm;
    sqlstm.sqlvsn = 13;
    sqlstm.arrsiz = 7;
    sqlstm.sqladtp = &sqladt;
    sqlstm.sqltdsp = &sqltds;
    sqlstm.iters = (unsigned int  )1;
    sqlstm.offset = (unsigned int  )187;
    sqlstm.cud = sqlcud0;
    sqlstm.sqlest = (unsigned char  *)&sqlca;
    sqlstm.sqlety = (unsigned short)4352;
    sqlstm.occurs = (unsigned int  )0;
    sqlcxt((void **)0, &sqlctx, &sqlstm, &sqlfpn);
}


			}
		}
		DisConnectSocket (cSockId);
		fflush (stdout);

	    /*-------------------------------------------------------*/
	    /*   시스템/서버별 Agent 상태 변경                       */
	    /*-------------------------------------------------------*/
		/* EXEC SQL
			UPDATE  CMM0031
		       SET  CM_Agent = :sRstCond
         	 WHERE  CM_SysCD = :ssyscd
           	   AND  CM_SvrCD = :sSvrcd
           	   AND  CM_SvrIP = TRIM(:ServerIP)
           	   AND	CM_PORTNO = :SvrPort
           	   AND  CM_SVRSTOP = 'N'
           	   AND  CM_CLOSEDT IS NULL; */ 

{
  struct sqlexd sqlstm;
  sqlstm.sqlvsn = 13;
  sqlstm.arrsiz = 7;
  sqlstm.sqladtp = &sqladt;
  sqlstm.sqltdsp = &sqltds;
  sqlstm.stmt = "update CMM0031  set CM_Agent=:b0 where (((((CM_SysCD=:b1 a\
nd CM_SvrCD=:b2) and CM_SvrIP=trim(:b3)) and CM_PORTNO=:b4) and CM_SVRSTOP='N\
') and CM_CLOSEDT is null )";
  sqlstm.iters = (unsigned int  )1;
  sqlstm.offset = (unsigned int  )202;
  sqlstm.cud = sqlcud0;
  sqlstm.sqlest = (unsigned char  *)&sqlca;
  sqlstm.sqlety = (unsigned short)4352;
  sqlstm.occurs = (unsigned int  )0;
  sqlstm.sqhstv[0] = (unsigned char  *)sRstCond;
  sqlstm.sqhstl[0] = (unsigned long )6;
  sqlstm.sqhsts[0] = (         int  )0;
  sqlstm.sqindv[0] = (         short *)0;
  sqlstm.sqinds[0] = (         int  )0;
  sqlstm.sqharm[0] = (unsigned long )0;
  sqlstm.sqadto[0] = (unsigned short )0;
  sqlstm.sqtdso[0] = (unsigned short )0;
  sqlstm.sqhstv[1] = (unsigned char  *)ssyscd;
  sqlstm.sqhstl[1] = (unsigned long )6;
  sqlstm.sqhsts[1] = (         int  )0;
  sqlstm.sqindv[1] = (         short *)0;
  sqlstm.sqinds[1] = (         int  )0;
  sqlstm.sqharm[1] = (unsigned long )0;
  sqlstm.sqadto[1] = (unsigned short )0;
  sqlstm.sqtdso[1] = (unsigned short )0;
  sqlstm.sqhstv[2] = (unsigned char  *)sSvrcd;
  sqlstm.sqhstl[2] = (unsigned long )3;
  sqlstm.sqhsts[2] = (         int  )0;
  sqlstm.sqindv[2] = (         short *)0;
  sqlstm.sqinds[2] = (         int  )0;
  sqlstm.sqharm[2] = (unsigned long )0;
  sqlstm.sqadto[2] = (unsigned short )0;
  sqlstm.sqtdso[2] = (unsigned short )0;
  sqlstm.sqhstv[3] = (unsigned char  *)ServerIP;
  sqlstm.sqhstl[3] = (unsigned long )21;
  sqlstm.sqhsts[3] = (         int  )0;
  sqlstm.sqindv[3] = (         short *)0;
  sqlstm.sqinds[3] = (         int  )0;
  sqlstm.sqharm[3] = (unsigned long )0;
  sqlstm.sqadto[3] = (unsigned short )0;
  sqlstm.sqtdso[3] = (unsigned short )0;
  sqlstm.sqhstv[4] = (unsigned char  *)&SvrPort;
  sqlstm.sqhstl[4] = (unsigned long )sizeof(int);
  sqlstm.sqhsts[4] = (         int  )0;
  sqlstm.sqindv[4] = (         short *)0;
  sqlstm.sqinds[4] = (         int  )0;
  sqlstm.sqharm[4] = (unsigned long )0;
  sqlstm.sqadto[4] = (unsigned short )0;
  sqlstm.sqtdso[4] = (unsigned short )0;
  sqlstm.sqphsv = sqlstm.sqhstv;
  sqlstm.sqphsl = sqlstm.sqhstl;
  sqlstm.sqphss = sqlstm.sqhsts;
  sqlstm.sqpind = sqlstm.sqindv;
  sqlstm.sqpins = sqlstm.sqinds;
  sqlstm.sqparm = sqlstm.sqharm;
  sqlstm.sqparc = sqlstm.sqharc;
  sqlstm.sqpadto = sqlstm.sqadto;
  sqlstm.sqptdso = sqlstm.sqtdso;
  sqlcxt((void **)0, &sqlctx, &sqlstm, &sqlfpn);
}



		/* EXEC SQL COMMIT; */ 

{
  struct sqlexd sqlstm;
  sqlstm.sqlvsn = 13;
  sqlstm.arrsiz = 7;
  sqlstm.sqladtp = &sqladt;
  sqlstm.sqltdsp = &sqltds;
  sqlstm.iters = (unsigned int  )1;
  sqlstm.offset = (unsigned int  )237;
  sqlstm.cud = sqlcud0;
  sqlstm.sqlest = (unsigned char  *)&sqlca;
  sqlstm.sqlety = (unsigned short)4352;
  sqlstm.occurs = (unsigned int  )0;
  sqlcxt((void **)0, &sqlctx, &sqlstm, &sqlfpn);
}


    }
    /* EXEC SQL close SYSInfo; */ 

{
    struct sqlexd sqlstm;
    sqlstm.sqlvsn = 13;
    sqlstm.arrsiz = 7;
    sqlstm.sqladtp = &sqladt;
    sqlstm.sqltdsp = &sqltds;
    sqlstm.iters = (unsigned int  )1;
    sqlstm.offset = (unsigned int  )252;
    sqlstm.cud = sqlcud0;
    sqlstm.sqlest = (unsigned char  *)&sqlca;
    sqlstm.sqlety = (unsigned short)4352;
    sqlstm.occurs = (unsigned int  )0;
    sqlcxt((void **)0, &sqlctx, &sqlstm, &sqlfpn);
}



	fprintf(stdout, "\n");
	fprintf(stdout, " ┌───────────────────────────────────────────┐\n");
	fprintf(stdout, " │           CHECK 결과 --> 대상서버 : %3d대,  정상 : %3d대,  장애 : %3d대              │\n", nTotCnt, nOKCnt, nErrCnt);
 	fprintf(stdout, " └───────────────────────────────────────────┘\n");
	fprintf(stdout, "\n");
	fprintf(stdout, "\n");

    /* EXEC SQL COMMIT WORK RELEASE; */ 

{
    struct sqlexd sqlstm;
    sqlstm.sqlvsn = 13;
    sqlstm.arrsiz = 7;
    sqlstm.sqladtp = &sqladt;
    sqlstm.sqltdsp = &sqltds;
    sqlstm.iters = (unsigned int  )1;
    sqlstm.offset = (unsigned int  )267;
    sqlstm.cud = sqlcud0;
    sqlstm.sqlest = (unsigned char  *)&sqlca;
    sqlstm.sqlety = (unsigned short)4352;
    sqlstm.occurs = (unsigned int  )0;
    sqlcxt((void **)0, &sqlctx, &sqlstm, &sqlfpn);
}



}

/*---------------------------------------------------------------*/
/*             E N D    O F    P R O G R A M                     */
/*---------------------------------------------------------------*/
