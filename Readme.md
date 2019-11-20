
### Steps to execute

1. Simply clone the repository and open as maven project in any of your Favourite IDE (intellij, eclipse)
2. All tests are in `src/test/java/TraditionalTests.java` Or `src/test/java/VisualAITests.java`
3. Set the key for your account in system environment variable `APPILIKEY`
4. By default test will run against version1 of the app, to run against version2 of app, 
simply goto BaseTest.java file and change assignment of baseUrl to `baseUrl= hackathonV2;`
5. Open any of the test file and directly run those tests from your IDE

Note: Purpose of this repository is to explore the difference between writing UI test traditional vs Visual way. One could have done more optimization around framework like providing version of app to run against via maven commandline Or having a testNG xml etc but this was not the purpose of the framework.
