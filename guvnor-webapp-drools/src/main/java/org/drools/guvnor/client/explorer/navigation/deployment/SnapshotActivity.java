package org.drools.guvnor.client.explorer.navigation.deployment;

import com.google.gwt.event.shared.EventBus;
import org.drools.guvnor.client.common.GenericCallback;
import org.drools.guvnor.client.common.LoadingPopup;
import org.drools.guvnor.client.explorer.AcceptItem;
import org.drools.guvnor.client.explorer.ClientFactory;
import org.drools.guvnor.client.messages.Constants;
import org.drools.guvnor.client.moduleeditor.drools.SnapshotView;
import org.drools.guvnor.client.rpc.Module;
import org.drools.guvnor.client.rpc.RepositoryServiceFactory;
import org.drools.guvnor.client.rpc.SnapshotInfo;
import org.drools.guvnor.client.util.Activity;

public class SnapshotActivity extends Activity {

    private final ClientFactory clientFactory;
    private final String moduleName;
    private final String snapshotName;

    public SnapshotActivity(String moduleName,
                            String snapshotName,
                            ClientFactory clientFactory) {
        this.moduleName = moduleName;
        this.snapshotName = snapshotName;
        this.clientFactory = clientFactory;
    }

    @Override
    public void start(final AcceptItem tabbedPanel, final EventBus eventBus) {
        clientFactory.getModuleService().loadSnapshotInfo(
                moduleName,
                snapshotName,
                new GenericCallback<SnapshotInfo>() {
                    public void onSuccess(SnapshotInfo snapshotInfo) {
                        showTab( tabbedPanel, snapshotInfo, eventBus );
                    }
                } );
    }

    private void showTab(final AcceptItem tabbedPanel, final SnapshotInfo snapshotInfo, final EventBus eventBus) {

        LoadingPopup.showMessage( Constants.INSTANCE.LoadingSnapshot() );

        RepositoryServiceFactory.getPackageService().loadModule( snapshotInfo.getUuid(),
                new GenericCallback<Module>() {
                    public void onSuccess(Module conf) {
                        tabbedPanel.add( Constants.INSTANCE.SnapshotLabel( snapshotInfo.getName() ),
                                new SnapshotView(
                                        clientFactory,
                                        eventBus,
                                        snapshotInfo,
                                        conf ) );
                        LoadingPopup.close();
                    }
                } );

    }

}
