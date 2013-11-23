

$TOMCAT_PKG    = 'tomcat7'
$CATALINA_BASE = "/var/lib/${TOMCAT_PKG}"
$CATALINA_HOME = "/usr/share/${TOMCAT_PKG}"
$JAVA_HOME     = '/usr/lib/jvm/default-java/jre'
$LOGIT_VERSION = '0.5.7'

exec {
    'apt-get_update':
        command     => '/usr/bin/apt-get update',
        logoutput   => on_failure,
        onlyif      => ['test -f /etc/apt/sources.list'],
        path        => '/usr/bin:/usr/sbin:/bin:/usr/local/bin',
        #refreshonly => true,
}

package {
    [$TOMCAT_PKG,'vim'
    ,'python-zmq','libzmq1' # Only needed to test python test script locally
    ]:
        ensure      => installed,
        provider    => apt,
        require     => Exec['apt-get_update'],
}


file { "${CATALINA_BASE}/bin":
        ensure      => directory,
        require     => Package[$TOMCAT_PKG],
}

file { "${CATALINA_BASE}/bin/setenv.sh":
        ensure      => present,
        content     => "# Add logit.jar to classpath
if [ -r \"${JAVA_HOME}/lib/ext/logit.jar\" ] ; then
  CATALINA_OPTS=\"${CATALINA_OPTS} -Dlogit.debug -Djava.security.egd=file:/dev/./urandom\"
fi
",
        mode        => '0755',
        require     => File["${CATALINA_BASE}/bin"],
}

service { $TOMCAT_PKG:
        ensure      => running,
        subscribe   => File["${CATALINA_HOME}/lib/logit-tomcatvalve.jar"],
}

file { "${JAVA_HOME}/lib/ext/logit.jar":
        ensure      => link,
        target      => "/vagrant/target/logit-${LOGIT_VERSION}-jar-with-dependencies.jar",
        require     => Package[$TOMCAT_PKG],
}

file { "${CATALINA_HOME}/lib/logit-tomcatvalve.jar":
        ensure      => link,
        target      => "/vagrant/target/logit-${LOGIT_VERSION}-tomcatvalve.jar",
        require     => Package[$TOMCAT_PKG],
}

file { "${CATALINA_BASE}/conf/logging.properties":
        ensure      => link,
        target      => '/vagrant/src/test/resources/logging.properties',
        require     => Package[$TOMCAT_PKG],
}

file { "${CATALINA_BASE}/conf/server.xml":
        ensure      => link,
        target      => '/vagrant/src/test/resources/server.xml',
        require     => Package[$TOMCAT_PKG],
}
