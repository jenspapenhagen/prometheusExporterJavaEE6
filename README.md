# prometheus_Exporter for Java EE 6 Applications
This is a prometheus exporter for old Java EE6 Stack.

I hope this will help someone with legacy code.

output:
http://localhost:8080/test/metrics

```java
prometheusexport_jvmusedmemory_bytes_size XX.0
prometheusexport_jvmavailablememory_bytes_size XXX.0
prometheusexport_bootup_seconds_time 1567710109
prometheusexport_systemLoad_systemLoad not available
prometheusexport_availableProcessors_count_total 12
prometheusexport_osName_ CentOS 
prometheusexport_osArchitecture_ amd64
prometheusexport_osVersion_ 7
prometheusexport_tread_count_total XXX
prometheusexport_tread_count_peak XXX
prometheusexport_classLoaded_count_total 22128
prometheusexport_metricscalls_count_total 5
```

in MetricsResource.java a small example is shown.
