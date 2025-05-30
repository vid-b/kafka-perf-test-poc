# Kafka Cluster Performance Testing POC

This POC (Proof of Concept) application aims to test the performance of a Kafka cluster (using a simple sample application) under varying loads and configurations. The primary goal is to support scaling of instances, memory allocations, and consumer groups by tuning these parameters. This helps evaluate how well the Kafka cluster handles different scenarios and identify optimal configuration values for CPU performance and other key metrics.

---

##  About Direct Memory Configuration

- **Default Direct Memory Size:** Kafka uses direct memory for fast processing (e.g., SSL handshake buffers, message buffers). However, the default size is very small (~10MB), which is insufficient when scaling to a large number of consumers.
- **Recommendation:** Increase the direct memory size (e.g., from 10MB to 600MB) and configure Netty not to use direct memory excessively.
- **Reasoning:** Our aim is to test the **maximum number of consumers** Kafka can support, not the **speed of processing**. Heap memory will be used primarily for processing, and direct memory usage will be minimized.
- **Impact:** Direct memory exhaustion can prematurely cap the number of active consumers, even when heap memory is available. For example:
    - Allocating 8GB heap + increasing direct memory to 600MB allows ~2000 consumers.
    - If not configured properly, only ~250 consumers may run due to direct memory limits, despite available heap.

---

##  How to Test

1. **Deployment:**
    - Adjust the required parameter values in the `mta.yaml` file.
    - Deploy the application with the updated parameters.

2. **Runtime Tuning:**
    - You can dynamically change configuration values by updating user-defined environment variables.
    - Restart the application for changes to take effect.

3. **Consumer Distribution:**
    - Total number of consumers will be distributed evenly across all instances.
    - Each instance runs a defined number of consumers. For example:
        - If there are **400 consumers** and **4 instances**, each instance runs **100 consumers**.
        - Each consumer is named as:
          ```
          test-consumer-group-<instance_index>-<consumer_count_index>
          ```
        - Example distribution:
            - Instance 0: `cg-0` to `cg-99`
            - Instance 1: `cg-100` to `cg-199`
            - Instance 2: `cg-200` to `cg-299`
            - Instance 3: `cg-300` to `cg-399`

4. **Producing Messages:**
    - Use the REST API exposed by the application to produce messages:
      ```
      <host>/produce/{number_of_messages_to_be_produced}
      ```

5. **Monitoring:**
    - Use Grafana to monitor CPU performance and other relevant metrics.

---

## 🔧 Summary

This POC helps identify the Kafka cluster’s scalability boundaries by tuning memory and consumer configurations. The insights gained will aid in optimizing system resource utilization for production deployments.
