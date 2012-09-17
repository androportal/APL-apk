#!/system/bin/sh

export bin=/system/bin
export PATH=$bin:/usr/bin:/usr/sbin:/bin:$PATH
export TERM=linux
export HOME=/root
export MNT=/data/local/linux
export EG=/data/example
export SDCARD=/mnt/sdcard/APL
export WWW=/data/local/linux/var/www/html

FLAG=1

while [ $FLAG -eq 1 ]

do
    
    while [ $(ps|busybox grep com.aakash.lab |busybox wc -l) -eq 1 ]
    
        do
            busybox mkdir -p $MNT

            if [ -f /mnt/sdcard/apl.img ]
            then
                busybox mount -o loop /mnt/sdcard/apl.img /data/local/linux 
            elif [ -f /mnt/extsd/apl.img ]
            then
                busybox mount -o loop /extsd/apl.img /data/local/linux  
            else
                exit 1  
            fi

        	
            if [ ! -d ${SDCARD} ] || [ ! -d ${WWW} ] || [ ! -d ${EG} ]
   
                then                                                                  
	                busybox mkdir -p ${SDCARD}/c/code                                         
            	    busybox mkdir -p ${WWW}/c/code                                            
            	    busybox mkdir -p ${EG}/c

            	    busybox mkdir -p ${SDCARD}/cpp/code                                       
            	    busybox mkdir -p ${WWW}/cpp/code                                          
            	    busybox mkdir -p ${EG}/cpp

            	    busybox mkdir -p ${SDCARD}/python/code                                     
            	    busybox mkdir -p ${WWW}/python/code     
            	    busybox mkdir -p ${EG}/python

            	    busybox mkdir -p ${SDCARD}/scilab/code
            	    busybox mkdir -p ${WWW}/scilab/code   
            	    busybox mkdir -p ${EG}/scilab

            	    busybox mkdir -p ${SDCARD}/scilab/image
            	    busybox mkdir -p ${WWW}/scilab/image   
        	fi

#        	busybox  chroot  $MNT /bin/bash -c "mkdir /proc"
        	busybox mount -t proc proc $MNT/proc
#        	busybox  chroot  $MNT /bin/bash -c "mkdir /sys"
#        	busybox  chroot  $MNT /bin/bash -c "mkdir -p /dev/pts"
#        	busybox  chroot  $MNT /bin/bash -c "chown -R www-data.www-data /var/www/"
#        	busybox  chroot  $MNT /bin/bash -c "chmod -R a+x /usr/lib/cgi-bin"

	
        	busybox mount -o bind /dev $MNT/dev
        	busybox mount -t sysfs sysfs $MNT/sys
	
        	busybox  chroot  $MNT /bin/bash -c "echo 1 > /proc/sys/vm/drop_caches"
        	busybox  chroot  $MNT /bin/bash -c "rm /dev/null"
        	busybox  chroot  $MNT /bin/bash -c "rm /var/run/apache2/*"
        	busybox  chroot  $MNT /bin/bash -c "rm /var/run/apache2.pid"
        	busybox  chroot  $MNT /bin/bash -c "service apache2 stop"
        	busybox  chroot  $MNT /bin/bash -c "service apache2 start"
        	busybox  chroot  $MNT /bin/bash -c "rm /tmp/.X0-*" 
        	busybox  chroot  $MNT /bin/bash -c "mount /dev/pts" 
        	busybox  chroot  $MNT /bin/bash -c "nohup Xvfb :0 -screen 0 640x480x24 -ac < /dev/null > Xvfb.out 2> Xvfb.err &"
        	busybox  chroot  $MNT /bin/bash -c "shellinaboxd --localhost-only -t -s /:www-data:www-data:/:true &"
        	busybox  chroot  /data/local/linux /bin/bash -c "nohup python /root/sb_manage.py &>'/dev/null'&"
        	
            busybox mount -o bind ${SDCARD}/c/code ${WWW}/c/code
        	busybox mount -o bind ${SDCARD}/cpp/code ${WWW}/cpp/code
        	busybox mount -o bind ${SDCARD}/python/code ${WWW}/python/code
           	busybox mount -o bind ${SDCARD}/scilab/code ${WWW}/scilab/code
        	busybox mount -o bind ${SDCARD}/scilab/image ${WWW}/scilab/image
    
	        busybox mount -o bind ${EG}/c ${WWW}/c/exbind
        	busybox mount -o bind ${EG}/cpp ${WWW}/cpp/exbind
            busybox mount -o bind ${EG}/python ${WWW}/python/exbind
           	busybox mount -o bind ${EG}/scilab ${WWW}/scilab/exbind
    
        	busybox  chroot  $MNT /bin/bash -c "chmod 777 /var/www/html/c/exbind/.open_file.c"
        	busybox  chroot  $MNT /bin/bash -c "chmod 777 /var/www/html/cpp/exbind/.open_file.cpp"
        	busybox  chroot  $MNT /bin/bash -c "chmod 777 /var/www/html/python/exbind/.open_file.py"
        	busybox  chroot  $MNT /bin/bash -c "chmod 777 /var/www/html/scilab/exbind/.open_file.cde"

    	   	FLAG=0
        	exit 0 
    done
    sleep 1 

done
exit 0
