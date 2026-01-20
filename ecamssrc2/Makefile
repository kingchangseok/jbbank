# ┌──────┬───────────────────────┐
# │ 프로그램명 │ Makefile                                     │
# ├──────┼───────────────────────┤
# │ 기      능 │ Oracle Pro*C Compile Script                  │
# ├──────┼───────────────────────┤
# │ 작  성  일 │ 2007. 10. 25                                 │
# ├──────┼───────────────────────┤
# │ 작  성  자 │ 최   병   남                                 │
# └──────┴───────────────────────┘

include $(ORACLE_HOME)/precomp/lib/env_precomp.mk

eCAMS_HOME=$(HOME)
BINPATH = $(eCAMS_HOME)/bin
LIBPATH = $(eCAMS_HOME)/lib
INCLIB  = $(eCAMS_HOME)/inc

COMMAR    = $(LIBPATH)/libecams_comm.a
ACCTAR    = $(LIBPATH)/libecams_acct.a
ACCOMPAR  = $(LIBPATH)/libecams_accomp.a
COMMLIB   = -L$(LIBPATH) -lecams_comm
ACCTLIB   = -L$(LIBPATH) -lecams_acct
ACCOMPLIB = -L$(LIBPATH) -lecams_accomp
ORALIB    = -L$(LIBHOME)/ $(PROLDLIBS)

PROC=$(ORACLE_HOME)/bin/proc
USERID=$(DBUSER)/$(DBPASS)@$(DBCONN)
PROCFLAG= SQLCHECK=full userid=$(USERID) include=$(INCLIB) unsafe_null=yes mode=oracle dbms=v8 define=_H_SYSRAS define=_H_BUF
CFLAGS= -DLOGGING -g
LIBHOME=$(ORACLE_HOME)/lib/
LD_LIBRARY_PATH=$(ORACLE_HOME)/lib:/usr/lib/


CC=cc -D MULTIARG -D TEST_MODE -D ON_LINE -D LOGGING -I$(INCLIB) --no-warn
CCcurses=cc
AR=ar rc
LIBcurses=-lcurses -ltermcap

all:						\
		arall               \
		binall				\


arall:						\
		commar              \
		acctar				\
		accompar   			\

binall:                  		\
		ecams_mgr       		\
		ecams_acct				\
		ecams_accomp			\
		FileUpdate_cmr0025  	\
		FileWrite_cmr0027   	\
		FileWrite_devcmr0025	\
		FileRead_devcmr0025		\
		FileRead_cmr0025		\
		FileDown_cmr0025		\
		FileRead_cmr0027		\
		ecams_cmm0070			\
		ecams_gensrc			\
		ecams_cmpsrc			\
		ecams_md5update			\
		ecams_sendmsg			\
		ecams_sendmsg_sub		\
		ecams_prcanal			\
		ecams_svrck         	\
		ecams_clragentlog		\
		ecams_bkdata			\
		ecams_req_maint			\
		ecams_verfile       	\
		ecams_cmm0040       	\
		ecams_difflist      	\
		ecams_diffck			\
		ecams_sync          	\
		ecams_srccut			\
		ecams_mci				\
		ecams_eai				\
		ecams_parm				\
		ecams_trcontrol			\
		ecams_trcontrol_sub		\
		ecams_trcontrolck		\
		ecams_trcontrolck_sub	\
		ecams_checkin_sub		\
		ecams_autorsrc			\
		ecams_autorsrc_sub		\
		ecams_lockck			\
		ecams_parmeff			\
		FileRead_devcmr0025_rsa	\
		ecams_InsertInnerData	\
		FileRead_devcmr0025_ver	\
		ecams_gitlab_export     \
#		ecams_backupmv			\

commar:                 	\
		ecams_common     	\


acctar:                 	\
		acct_lib        	\
		ecams_acct_sysdn	\
		ecams_acct_sysdnc   \
		ecams_acct_syscn    \
		ecams_acct_sysal	\
		ecams_acct_sysanl	\
		ecams_acct_sysar	\
		ecams_acct_sysced	\
		ecams_acct_sysdl	\
		ecams_acct_sysmv	\
		ecams_acct_sysrmv	\
		ecams_acct_syscom	\
		ecams_acct_sysed	\
		ecams_acct_sysfmk	\
		ecams_acct_sysgb	\
		ecams_acct_syspf	\
		ecams_acct_sysup	\
		ecams_acct_sysft	\
		ecams_acct_syspmd	\
		ecams_acct_verfile  \
		ecams_acct_sysdnrmv \
		ecams_acct_sysdnmv	\
		ecams_acct_sysjar	\
		ecams_acct_syseff	\



accompar:                   \
		accomp_lib          \
		ecams_accomp_comp	\
		ecams_accomp_put	\
		ecams_accomp_get	\
		ecams_accomp_tput	\


.SUFFIXES: .pc .c .o

clean:
	rm -f *.o core

.c.o:
	$(CC)  $(CFLAGS) -c $*.c -o $@ -I$(INCLIB)


# ############################################################
# ORACLE PRO*C
# ############################################################
ecams_cmpsrc          	\
ecams_sendmsg_sub	  	\
ecams_sendmsg         	\
FileUpdate_cmr0025    	\
ecams_cmm0070		  	\
ecams_gensrc		  	\
ecams_md5update		  	\
ecams_svrck			  	\
ecams_clragentlog	  	\
ecams_bkdata    	  	\
ecams_req_maint		  	\
ecams_cmm0040		  	\
ecams_difflist        	\
ecams_fortify		  	\
ecams_vermake		  	\
:
	$(PROC) $(PROCFLAG) iname=$@.pc oname=$@.c
	$(CC) $@.c $(COMMLIB) $(ORALIB) -o $@
	rm $@.c $@.lis
	mv $@ $(BINPATH)

ecams_acct            	\
ecams_mgr             	\
FileWrite_cmr0027     	\
FileWrite_devcmr0025  	\
FileRead_devcmr0025	  	\
FileRead_cmr0025	  	\
FileDown_cmr0025	  	\
FileRead_cmr0027      	\
ecams_prcanal		  	\
ecams_verfile         	\
ecams_diffck		  	\
ecams_sync            	\
ecams_sysft_test	  	\
ecams_srccut		  	\
ecams_mci			  	\
ecams_eai			  	\
ecams_parm			  	\
ecams_trcontrol		  	\
ecams_trcontrol_sub	  	\
ecams_trcontrolck	  	\
ecams_trcontrolck_sub 	\
ecams_checkin_sub	  	\
ecams_autorsrc		  	\
ecams_autorsrc_sub		\
ecams_lockck		  	\
ecams_parmeff		  	\
ecams_acct_test		  	\
ecams_verchk		  	\
ecams_backupmv		  	\
FileRead_devcmr0025_rsa	\
ecams_InsertInnerData	\
FileRead_devcmr0025_ver	\
ecams_gitlab_export 	\
:
	$(PROC) $(PROCFLAG) iname=$@.pc oname=$@.c
	$(CC) $@.c $(COMMLIB) $(ACCTLIB) $(ORALIB) -o $@ 
	rm $@.c $@.lis
	mv $@ $(BINPATH)


ecams_accomp          \
:
	$(PROC) $(PROCFLAG) iname=$@.pc oname=$@.c
	$(CC) $@.c $(COMMLIB) $(ACCOMPLIB) $(ORALIB) -o $@
	rm $@.c $@.lis
	mv $@ $(BINPATH)

# ############################################################
# 아카이브 파일
# ############################################################
ecams_common        \
:
	$(PROC) $(PROCFLAG) iname=$@.pc
	$(CC) -c $@.c
	$(AR) $(COMMAR) $@.o
	rm $@.c $@.o $@.lis


acct_lib            \
ecams_acct_sysdn	\
ecams_acct_sysdnc   \
ecams_acct_syscn    \
ecams_acct_sysal	\
ecams_acct_sysanl	\
ecams_acct_sysar	\
ecams_acct_sysced	\
ecams_acct_sysdl	\
ecams_acct_sysmv	\
ecams_acct_sysrmv	\
ecams_acct_syscom	\
ecams_acct_sysed	\
ecams_acct_sysfmk	\
ecams_acct_sysgb	\
ecams_acct_syspf	\
ecams_acct_sysup	\
ecams_acct_sysft	\
ecams_acct_syspmd	\
ecams_acct_verfile	\
ecams_acct_sysdnrmv	\
ecams_acct_sysdnmv	\
ecams_acct_sysjar	\
ecams_acct_syseff	\
:
	$(PROC) $(PROCFLAG) iname=$@.pc
	$(CC) -c $@.c
	$(AR) $(ACCTAR) $@.o
	rm $@.c $@.o $@.lis


accomp_lib          \
ecams_accomp_comp	\
ecams_accomp_put	\
ecams_accomp_get	\
ecams_accomp_tput	\
:
	$(PROC) $(PROCFLAG) iname=$@.pc
	$(CC) -c $@.c
	$(AR) $(ACCOMPAR) $@.o
	rm $@.c $@.o $@.lis


