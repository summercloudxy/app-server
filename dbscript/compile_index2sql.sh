FTP_SERVER=${FTP_SERVER:-'192.168.5.28 21'}
FTP_USER=${FTP_USER:-'jenkins01'}
FTP_PASS=${FTP_PASS:-'jenkins01'}
FTP_FOLDER_DB=${FTP_FOLDER_DB:-'/xg_smartfactory2/component/db'}

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

### loop and down all
for aFile in $files
do
echo "To ftp download: $aFile"
ftp -ivn << EOF
open $FTP_SERVER
user $FTP_USER  $FTP_PASS
bin
cd $FTP_FOLDER_DB
ls
get $aFile
bye
EOF

done