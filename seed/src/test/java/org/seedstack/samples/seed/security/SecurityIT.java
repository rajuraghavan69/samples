package org.seedstack.samples.seed.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.seed.Configuration;
import org.seedstack.seed.it.SeedITRunner;
import org.seedstack.seed.security.AuthorizationException;
import org.seedstack.seed.security.RequiresPermissions;
import org.seedstack.seed.security.SecuritySupport;
import org.seedstack.seed.security.WithUser;

import javax.inject.Inject;

@RunWith(SeedITRunner.class)
public class SecurityIT {
    @Configuration("my-app.country")
    private String country;
    @Inject
    SecuritySupport securitySupport;

    @Test
    @WithUser(id = "alice", password = "password")
    public void testAliceCanDeleteUser() throws Exception {
        deleteUser("someone");
    }

    @Test(expected = AuthorizationException.class)
    @WithUser(id = "bob", password = "password")
    public void testBobCannotDeleteUser() throws Exception {
        deleteUser("someone");
    }

    @Test
    @WithUser(id = "carol", password = "password")
    public void testCarolCanEditArticle() throws Exception {
        editArticle("someArticle");
    }

    @Test(expected = AuthorizationException.class)
    @WithUser(id = "dave", password = "password")
    public void testDaveCannotEditArticle() throws Exception {
        editArticle("someArticle");
    }

    public void deleteUser(String userId) throws IllegalAccessException {
        if (!securitySupport.isPermitted("user:delete", new Location(country))) {
            throw new AuthorizationException("An administrator role for country " + country + " is required to delete an user");
        }
        System.out.println("Deleting user " + userId);
    }

    @RequiresPermissions("article:edit")
    public void editArticle(String articleId) {
        System.out.println("Editing article " + articleId);
    }
}
