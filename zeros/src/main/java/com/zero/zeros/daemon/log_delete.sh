cd /log/zero/
find . -type f -mtime +10 \! -name 'log_delete.sh' | xargs rm -f