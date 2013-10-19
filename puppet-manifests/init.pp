

$TOMCAT_PKG    = 'tomcat7'
$CATALINA_BASE = "/var/lib/${TOMCAT_PKG}"
$CATALINA_HOME = "/usr/share/${TOMCAT_PKG}"
$LOGIT_VERSION = '0.4.0-SNAPSHOT'

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
}

file { "${CATALINA_BASE}/bin/setenv.sh":
        ensure      => present,
        content     => '# Add logit.jar to classpath
if [ -r "$CATALINA_HOME/lib/logit.jar" ] ; then
  # CLASSPATH="$CLASSPATH:$CATALINA_BASE/bin/logit.jar"
  CATALINA_OPTS="$CATALINA_OPTS -Dlogit.debug"
fi
',
        mode        => '0755',
        require     => [File["${CATALINA_BASE}/bin"],Package[$TOMCAT_PKG]],
}

service { $TOMCAT_PKG:
        ensure      => running,
        subscribe   => File["${CATALINA_BASE}/bin/logit.jar"],
}

file { "${CATALINA_BASE}/bin/logit.jar":
        ensure      => link,
        target      => "/vagrant/target/logit-${LOGIT_VERSION}-jar-with-dependencies.jar",
        require     => File["${CATALINA_BASE}/bin"],
}

file { "${CATALINA_HOME}/lib/logit.jar":
        ensure      => link,
        target      => "/vagrant/target/logit-${LOGIT_VERSION}-jar-with-dependencies.jar",
        require     => Package[$TOMCAT_PKG],
}
