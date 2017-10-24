#!/bin/bash
rm all.sql
touch all.sql
chmod 777 all.sql
cd ddl
sqlfiles=`ls *.sql`
for sql in  $sqlfiles
do
  echo "For: $sql" 
  cat $sql >> ../all.sql
done
cd ..
cd dml
sqlfiles=`ls *.sql`
for sql in  $sqlfiles
do
  echo "For: $sql" 
  cat $sql >> ../all.sql
done
