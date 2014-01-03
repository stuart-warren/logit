# -*- mode: ruby -*-
# vi: set ft=ruby :

# Vagrantfile API/syntax version. Don't touch unless you know what you're doing!
VAGRANTFILE_API_VERSION = "2"


  #
  # Vagrant for logit:
  # --------
  # vagrant up
  # vagrant ssh
  # python /vagrant/src/test/resources/test.py -v &
  # sudo service tomcat7 restart
  # wget 'http://localhost:8080/testurl/?param1=value1'
  #


Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  # All Vagrant configuration is done here. The most common configuration
  # options are documented and commented below. For a complete reference,
  # please see the online documentation at vagrantup.com.

  # Every Vagrant virtual environment requires a box to build off of.
  config.vm.box = "precise64"

  # Create a public network, which generally matched to bridged network.
  # Bridged networks make the machine appear as another physical device on
  # your network.
  config.vm.network :public_network


  # Provision your VM
  config.vm.provision :shell, :inline => "echo \"Europe/London\" | sudo tee /etc/timezone && dpkg-reconfigure --frontend noninteractive tzdata"
  config.vm.provision :puppet do |puppet|
    puppet.manifests_path = "puppet-manifests"
    puppet.manifest_file  = "init.pp"
    puppet.options = "--verbose --debug"
  end

end
