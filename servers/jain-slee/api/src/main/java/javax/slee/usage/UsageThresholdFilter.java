package javax.slee.usage;

import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.NotificationSource;

/**
 * A notification filter that only allows through {@link UsageNotification}s where the
 * notification source usage parameter name match specified values, and the value of the
 * usage parameter has crossed a specified threshold (in either the positive or negative
 * direction).
 * <p>
 * Each time a usage notification is seen for the specified usage parameter, the current
 * value of the usage parameter is stored.  If the previous value and the current value
 * lie on opposite sides of the threshold, the notification is passed to notification
 * listeners.  If the previous value was equal to the threshold value, the notification
 * is only passed to notification listeners if the direction on the real axis from the
 * previous value to the current value is the same as the approach made to the threshold
 * value.
 * <p>
 * For example, if the threshold value is set to 10, and three usage notifications with
 * the values 9, 10, and 9 respectively are generated by the SLEE, this notification
 * filter would not pass any of the usage notifications to notification listeners as the
 * threshold has not been crossed.  However if three usage notifications with the values
 * 9, 10, and 11 respectively are generated by the SLEE, this notification filter would
 * pass only the last usage notification to notification listeners, as this represents
 * the point when the threshold has been crossed.
 * <p>
 * In any other situation where the threshold value is not crossed, the notification is
 * suppressed.
 * <p>
 * If the notification contains usage information for some other notification source or
 * usage parameter, it is also suppressed.
 * <p>
 * Notifications that are not instances of {@link UsageNotification} are also suppressed
 * by this filter.
 */
public class UsageThresholdFilter implements NotificationFilter {
    /**
     * Create a <code>UsageThresholdFilter</code>.  A filter created using this constructor will
     * only allow SLEE 1.0-compliant usage notifications to pass through where they otherwise
     * satisfy the filtering criteria.
     * @param service the component identifier of the Service whose usage parameter
     *        should be monitored.
     * @param sbb the component identifier of the SBB whose usage parameter should be
     *        monitored.
     * @param paramName the name of a usage parameter defined by the SBB.
     * @param threshold the threshold value.
     * @throws NullPointerException if <code>service</code>, <code>sbb</code>, or
     *        <code>paramName</code> is <code>null</code>.
     * @deprecated Replaced with {@link #UsageThresholdFilter(NotificationSource, String, long)}
     *        as usage collecting has been expanded to include SLEE components other than SBBs.
     */
    public UsageThresholdFilter(ServiceID service, SbbID sbb, String paramName, long threshold) throws NullPointerException {
        if (service == null) throw new NullPointerException("service is null");
        if (sbb == null) throw new NullPointerException("sbb is null");
        if (paramName == null) throw new NullPointerException("paramName is null");

        this.service = service;
        this.sbb = sbb;
        this.paramName = paramName;
        this.threshold = threshold;

        // forward compatibiltiy
        this.notificationSource = null;
    }

    /**
     * Create a <code>UsageThresholdFilter</code>.  A filter created using this constructor will
     * only allow SLEE 1.1-compliant usage notifications to pass through where they otherwise
     * satisfy the filtering criteria.
     * @param notificationSource the notification source whose usage parameter should be
     *        monitored.
     * @param paramName the name of a usage parameter defined by the SBB.
     * @param threshold the threshold value.
     * @throws NullPointerException if <code>notificationSource</code> or <code>paramName</code>
     *        is <code>null</code>.
     */
    public UsageThresholdFilter(NotificationSource notificationSource, String paramName, long threshold) throws NullPointerException {
        if (notificationSource == null) throw new NullPointerException("notificationSource is null");
        if (paramName == null) throw new NullPointerException("paramName is null");

        this.notificationSource = notificationSource;
        this.paramName = paramName;
        this.threshold = threshold;

        // backward compatibiltiy
        this.service = null;
        this.sbb = null;
    }

    /**
     * Determine whether the specified notification should be delivered to notification
     * listeners using this notification filter.
     * @param notification the notification to be sent.
     * @return <code>true</code> if the notification should be delivered to notification
     *        listeners, <code>false</code> otherwise.  This method always returns
     *        <code>false</code> if <code>notification</code> is not an instance of
     *        {@link UsageNotification}.
     */
    public boolean isNotificationEnabled(Notification notification) {
        if (!(notification instanceof UsageNotification)) return false;

        UsageNotification usageNotification = (UsageNotification)notification;
        if (service != null) {
            // SLEE 1.0 comparison
            if (!service.equals(usageNotification.getService())) return false;
            if (!sbb.equals(usageNotification.getSbb())) return false;
        }
        else {
            // SLEE 1.1 comparison
            if (!notificationSource.equals(usageNotification.getNotificationSource())) return false;
        }
        if (!usageNotification.getUsageParameterName().equals(paramName)) return false;

        long current = usageNotification.getValue();
        try {
            return (previous != threshold)
                ? ((previous < threshold && current > threshold) ||  // crossed +ve dir
                   (previous > threshold && current < threshold))    // crossed -ve dir
                : (getDirection(previous, current) == lastDir);      // crossed in last dir
        }
        finally {
            if (previous != current) {
                lastDir = getDirection(previous, current);
                previous = current;
            }
        }
    }

    /**
     * Get the axial direction of movement from <code>x</code> to <code>y</code>.
     * @return 1 if <code>x&lt;y</code>, -1 if <code>x&gt;y</code>, or 0 if <code>x==y</code>.
     */
    private int getDirection(long x, long y) {
        return x == y ? 0 : (x < y ? 1 : -1);
    }


    private final ServiceID service;
    private final SbbID sbb;
    private final NotificationSource notificationSource;
    private final String paramName;
    private final long threshold;
    private long previous = 0;
    private long lastDir = 0;
}

