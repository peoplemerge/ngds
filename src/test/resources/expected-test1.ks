install
nfs --server=192.168.10.101 --dir=/media/software/linux/distributions/centos/6.3/os/i386
lang en_US.UTF-8
keyboard us
network --onboot yes --device eth0 --mtu=1500 --bootproto dhcp --ipv6 auto --hostname test1
rootpw  --iscrypted $6$5LkqInxECgnHsOQC$YpNOLnnls29ngoFVzg2dq6Frnt79fnHk73hcB5RMH/tSuegP9lqKyjfkVz.wikYK5t5AHM.3z3y/Nu.i..Rct0
reboot
firewall --service=ssh
authconfig --enableshadow --passalgo=sha512
selinux --enforcing
timezone --utc America/Los_Angeles
bootloader --location=mbr --driveorder=vda --append="crashkernel=auto rhgb"
zerombr
autopart
logging --host=192.168.10.105

repo --name="CentOS"  --baseurl=nfs:192.168.10.101:/media/software/linux/distributions/centos/6.3/os/i386 --cost=100
repo --name="PeopleMerge"  --baseurl=nfs:192.168.10.101:/media/software/rpm/RPMS/i386 --cost=100

%packages --nobase
@core
zookeeper
libzookeeper
libzookeeper-devel
python-zookeeper
nfs-utils
openssh-clients
openssh
%post

mkdir /mnt/temp
echo mount 192.168.10.101:/media /mnt/temp >>/root/mount-out
mount -o nolock 192.168.10.101:/media /mnt/temp 2>> /root/mount-out
cp /mnt/temp/software/kickstart/createZkNodes.sh /root
umount /mnt/temp

cat >>/etc/hosts <<EOF
192.168.10.101  heracles
192.168.10.105  ino
EOF

cat >>/etc/rc.local <<EOF
/root/createZkNodes.sh
EOF

%end
