space="                                                          " 
for i in $(find target/multibanking-filesystem* -type f)
do
	echo $i
	plain=$(basename $i)
	if [[ $plain == KS-* ]]
	then
		 echo    "$space KEYSTORE CONTENT NOT VISIBLE"
	else
		unzip -o $i >/dev/null
		echo     "$space METADATA -------" 
		sed "s/^/$space/" StorageMetadata.json   
		echo     "$space CONTENT -------" 
		sed "s/^/$space/" Content.binary    
	fi
done
