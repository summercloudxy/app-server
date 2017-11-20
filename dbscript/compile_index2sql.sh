FTP_SERVER=${FTP_SERVER:-'192.168.5.28 21'}
FTP_USER=${FTP_USER:-'jenkins01'}
FTP_PASS=${FTP_PASS:-'jenkins01'}
FTP_FOLDER_DB=${FTP_FOLDER_DB:-'/xg_smartfactory2/component/db'}
PROFILE=${PROFILE:-'prod'}

### join all index into one
ALL_INDEX=all_1510901510.index
rm -f $ALL_INDEX
indexes=`ls *.index`
for i in  $indexes
do
  cat $i >> $ALL_INDEX
  echo '' >> $ALL_INDEX
done

files=`cat $ALL_INDEX`

### loop and generate down cmd
DOWN_CMD=download.cmdlist
echo "open $FTP_SERVER" > $DOWN_CMD
echo "user $FTP_USER  $FTP_PASS" >> $DOWN_CMD
echo "bin" >> $DOWN_CMD
echo "cd $FTP_FOLDER_DB" >> $DOWN_CMD

# append cmd list
for aFile in $files
do
  echo "get ${aFile}-${PROFILE}"  >> $DOWN_CMD
  echo '' >> $DOWN_CMD
done
echo "bye"  >> $DOWN_CMD

ftp -ivn < $DOWN_CMD

###
mkdir dist
mv *.sql dist
rm -f *.*
mv dist/* .
rm -rf dist
