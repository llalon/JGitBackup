# JGitBackup

Backup repos from GitHub or GitLab.

## Usage:

#### Run once

```bash
docker run -i --rm \
    -e JGITBACKUP_PROVIDER=github \
    -e JGITBACKUP_TOKEN=TOKEN \
    -e JGITBACKUP_USERNAME=USERNAME \
    -v /mnt/user/backup/git:/git docker.io/llalon/jgitbackup:latest
```

#### Run on schedule

```bash
docker run -i --rm \
    -e JGITBACKUP_PROVIDER=github \
    -e JGITBACKUP_TOKEN=TOKEN \
    -e JGITBACKUP_USERNAME=USERNAME \
    -e JGITBACKUP_SCHEDULE='5 * * * * *' \
    -v /mnt/user/backup/git:/git docker.io/llalon/jgitbackup:latest
```
## Configuration

For advanced configuration, including repo/owner whitelist. See `application.properties`.



