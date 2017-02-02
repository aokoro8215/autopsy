/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sleuthkit.autopsy.ingest.runIngestModuleWizard;

import java.util.HashSet;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.sleuthkit.autopsy.coreutils.ModuleSettings;

class RunIngestModuleWizardWizardPanel1 extends ShortCircuitableWizardPanel {

    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1);
    private final static String PROP_LASTPROFILE_NAME = "RIMW_LASTPROFILE_NAME"; //NON-NLS
    private final static String LAST_PROFILE_PROPERTIES_FILE = "IngestProfileSelectionPanel"; //NON-NLS
    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private IngestProfileSelectionPanel component;
    private String lastProfileUsed;

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public IngestProfileSelectionPanel getComponent() {
        if (component == null) {
            if (ModuleSettings.getConfigSetting(LAST_PROFILE_PROPERTIES_FILE, PROP_LASTPROFILE_NAME) == null
                    || ModuleSettings.getConfigSetting(LAST_PROFILE_PROPERTIES_FILE, PROP_LASTPROFILE_NAME).isEmpty()) {
                lastProfileUsed = RunIngestModuleWizardWizardIterator.getDefaultContext();
            } else {
                lastProfileUsed = ModuleSettings.getConfigSetting(LAST_PROFILE_PROPERTIES_FILE, PROP_LASTPROFILE_NAME);
            }
            component = new IngestProfileSelectionPanel(this, lastProfileUsed);
        }
        return component;
    }

    @Override
    boolean shouldCheckForNext() {
        return component.hasNextPanel;
    }

    @Override
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx("help.key.here");
    }

    @Override
    public boolean isValid() {
        // If it is always OK to press Next or Finish, then:
        return true;
        // If it depends on some condition (form filled out...) and
        // this condition changes (last form field filled in...) then
        // use ChangeSupport to implement add/removeChangeListener below.
        // WizardDescriptor.ERROR/WARNING/INFORMATION_MESSAGE will also be useful.
    }

    protected final void fireChangeEvent() {
        Set<ChangeListener> ls;
        synchronized (listeners) {
            ls = new HashSet<>(listeners);
        }
        ChangeEvent ev = new ChangeEvent(this);
        for (ChangeListener l : ls) {
            l.stateChanged(ev);
        }
    }

    @Override
    public void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    @Override
    public void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    @Override
    public void readSettings(WizardDescriptor wiz) {
    }

    @Override
    public void storeSettings(WizardDescriptor wiz) {
        lastProfileUsed = component.getLastSelectedProfile();
        wiz.putProperty("executionContext", lastProfileUsed); //NON-NLS
        ModuleSettings.setConfigSetting(LAST_PROFILE_PROPERTIES_FILE, PROP_LASTPROFILE_NAME, lastProfileUsed);
    }

}
