# JGitBackup

Backup repos form GitHub or GitLab.

Usage:

```bash
docker run -i --rm \
    -e JGITBACKUP_PROVIDER=github \
    -e JGITBACKUP_TOKEN=TOKEN \
    -e JGITBACKUP_USERNAME=USERNAME \
    -v /mnt/user/backup/git/github_llalon:/git docker.io/llalon/jgitbackup:latest
```

