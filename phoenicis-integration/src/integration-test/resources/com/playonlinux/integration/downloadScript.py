from com.playonlinux.integration import ServiceManagerGetter
from com.playonlinux.apps import AppsManager

import unittest, time, os

from com.playonlinux.apps import AppsEntitiesProvider
from com.playonlinux.mock import MockAppsEntitiesProviderObserver
from com.playonlinux.apps.entities import AppsCategoryEntity
from com.playonlinux.apps.entities import AppEntity
from com.playonlinux.core.filter import Filter


class AnyFilter(Filter):
    def apply(self, item):
        return True


class TestDownloadScript(unittest.TestCase):
    def testDownloadScript(self):

        # Set up the test environment
        ServiceManagerGetter().init("com.playonlinux.contexts.AppsManagerServicesContext")
        appsManager = ServiceManagerGetter.serviceManager.getService(AppsManager)
        appsEntityProvider = ServiceManagerGetter.serviceManager.getService(AppsEntitiesProvider)
        mockAppsEntitiesProviderObserver = MockAppsEntitiesProviderObserver()
        appsEntityProvider.addObserver(mockAppsEntitiesProviderObserver)

        # Waiting for the app list to be updated
        while(appsManager.isUpdating()):
            print "Updating available script list..."
            time.sleep(2)

        # Checking download status
        if(appsManager.hasFailed()):
            raise Exception("Failed to download the list of script")

        # Checking few categories
        categories = map(AppsCategoryEntity.getName, mockAppsEntitiesProviderObserver.getObserved().getCategoryDTOs()[:])
        self.assertTrue("Games" in categories)
        self.assertTrue("Accessories" in categories)
        self.assertTrue("Office" in categories)
        self.assertTrue("Graphics" in categories)

        # PlayOnLinux is not supposed to show any app if the users does not select a category
        apps = map(AppEntity.getName, mockAppsEntitiesProviderObserver.getObserved().getAppsItemDTOs()[:])
        self.assertEquals(0, len(apps))

        # Testing the filtering feature
        appsEntityProvider.applyFilter(AnyFilter())
        time.sleep(1)
        apps = map(AppEntity.getName, mockAppsEntitiesProviderObserver.getObserved().getAppsItemDTOs()[:])

        self.assertTrue("Steam" in apps)
        self.assertTrue("Microsoft Office 2010" in apps)



