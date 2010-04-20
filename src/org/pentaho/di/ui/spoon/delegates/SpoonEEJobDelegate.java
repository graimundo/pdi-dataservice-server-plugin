package org.pentaho.di.ui.spoon.delegates;

import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.ui.core.gui.GUIResource;
import org.pentaho.di.ui.repository.pur.services.ILockService;
import org.pentaho.di.ui.spoon.Spoon;
import org.pentaho.di.ui.spoon.TabMapEntry;
import org.pentaho.xul.swt.tab.TabItem;

public class SpoonEEJobDelegate extends SpoonJobDelegate{
  ILockService service;
  public SpoonEEJobDelegate(Spoon spoon) {
    super(spoon);
    Repository repository = spoon.getRepository();
    try {
      if(repository.hasService(ILockService.class)) {
        service = (ILockService) repository.getService(ILockService.class);
      }  else {
        throw new IllegalStateException();
      }
    } catch (KettleException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addJobGraph(JobMeta jobMeta) {
    super.addJobGraph(jobMeta);
    TabMapEntry tabEntry = spoon.delegates.tabs.findTabMapEntry(jobMeta);
    TabItem tabItem = tabEntry.getTabItem();
    try {
      if((service != null) && (jobMeta.getObjectId() != null) && (service.getJobLock(jobMeta.getObjectId()) != null)) {
        tabItem.setImage(GUIResource.getInstance().getImageLocked());
      }
    } catch(Exception e) {
      throw new RuntimeException(e);
    }
  }

}