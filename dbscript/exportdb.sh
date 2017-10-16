#!/bin/bash
DB_USER=root
DB_IP=192.168.5.34
DB_PW=123456
DB_NAME=smartfactory2
cd database_script/ddl
rm * 
cd ..
cd ..
mysqldump -u${DB_USER} -h ${DB_IP} -p${DB_PW} -d ${DB_NAME}|sed 's/AUTO_INCREMENT=[0-9]*\s//g' >database_script/ddl/smartfactory2_ddl.sql
cd database_script/dml
rm *
cd ..
cd ..
TABLES=`echo "select table_name from information_schema.tables where table_schema='smartfactory2'" | mysql -N -u ${DB_USER} -h ${DB_IP} -p${DB_PW} ${DB_NAME} `
array=(tb_thing11 tb_thing_properties11) #exclude files
for i in ${TABLES}
do
   if !(echo "${array[@]}" | grep -w ${i}) &>/dev/null; then
       mysqldump -t ${DB_NAME} -u${DB_USER} -h ${DB_IP} -p${DB_PW} --skip-extended-insert --tables ${i} >database_script/dml/${i}_dml.sql
   fi
done
