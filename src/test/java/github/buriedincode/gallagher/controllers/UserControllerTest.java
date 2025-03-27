package github.buriedincode.gallagher.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ActiveProfiles({"test"})
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Test
  public void createUserTest() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.post("/v1/create")).andExpect(status().isInternalServerError());
  }

  @Test
  public void deleteUserTest() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.delete("/v1/delete")).andExpect(status().isNotFound());
  }

  @Test
  public void readUserTest() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get("/v1/read")).andExpect(status().isInternalServerError());
  }

  @Test
  public void updateUserTest() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.put("/v1/update")).andExpect(status().isNotImplemented());
  }
}
