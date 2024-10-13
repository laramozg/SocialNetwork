//package controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.example.controller.ProfileServlet;
//import org.example.dto.ProfileDto;
//import org.example.service.ProfileService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.sql.SQLException;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class ProfileServletTest {
//    @Mock
//    private ProfileService profileService;
//    @Mock
//    private HttpServletRequest request;
//
//    @Mock
//    private HttpServletResponse response;
//    @Mock
//    private PrintWriter writer;
//
//    @InjectMocks
//    private ProfileServlet profileServlet;
//
//    private ObjectMapper objectMapper;
//
//
//    @BeforeEach
//    void setUp() throws IOException {
//        MockitoAnnotations.openMocks(this);
//        objectMapper = new ObjectMapper();
//        when(response.getWriter()).thenReturn(writer);
//    }
//
//    @Test
//    void testDoGet_ProfileFound() throws Exception {
//        ProfileDto profileDto = new ProfileDto(1, "John", "Doe", "john.doe@example.com",1);
//        when(request.getPathInfo()).thenReturn("/1");
//        when(profileService.getProfileByUserId(1)).thenReturn(profileDto);
//
//        profileServlet.doGet(request, response);
//
//        verify(response).setContentType("application/json");
//        verify(response).setStatus(HttpServletResponse.SC_OK);
//        verify(writer).write(objectMapper.writeValueAsString(profileDto));
//    }
//
//    @Test
//    void testDoGet_ProfileNotFound() throws Exception {
//        when(request.getPathInfo()).thenReturn("/1");
//        when(profileService.getProfileByUserId(1)).thenReturn(null);
//
//        profileServlet.doGet(request, response);
//
//        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
//        verify(writer).write("Profile not found");
//    }
//
//
//    @Test
//    void testDoPost_Success() throws Exception {
//        ProfileDto profileDto = new ProfileDto(1, "John", "Doe", "john.doe@example.com", 1);
//        String profileJson = objectMapper.writeValueAsString(profileDto);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(profileJson.getBytes())));
//        when(request.getReader()).thenReturn(reader);
//
//        when(profileService.saveProfile(any(ProfileDto.class))).thenReturn(1);
//
//        profileServlet.doPost(request, response);
//
//        verify(response).setStatus(HttpServletResponse.SC_CREATED);
//        verify(writer).write("Profile created with ID: 1");
//    }
//
//    @Test
//    void testDoPut_Success() throws Exception {
//        ProfileDto profileDto = new ProfileDto(1, "John", "Doe","john.doe@example.com", 1 );
//        String profileJson = objectMapper.writeValueAsString(profileDto);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(profileJson.getBytes())));
//        when(request.getReader()).thenReturn(reader);
//
//        profileServlet.doPut(request, response);
//
//        verify(profileService, times(1)).updateProfile(any(ProfileDto.class));
//        verify(response).setStatus(HttpServletResponse.SC_OK);
//        verify(writer).write("Profile updated successfully.");
//    }
//
//    @Test
//    void testDoDelete_Success() throws Exception {
//        when(request.getPathInfo()).thenReturn("/1");
//        profileServlet.doDelete(request, response);
//
//        verify(profileService, times(1)).deleteProfile(1);
//        verify(response).setStatus(HttpServletResponse.SC_NO_CONTENT);
//    }
//
//    @Test
//    void testDoDelete_InvalidId() {
//        when(request.getPathInfo()).thenReturn(null);
//
//        profileServlet.doDelete(request, response);
//
//        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        verify(writer).write("User ID is missing.");
//    }
//
//    @Test
//    void testDoGet_InternalServerError() throws Exception {
//        when(request.getPathInfo()).thenReturn("/1");
//        when(profileService.getProfileByUserId(1)).thenThrow(new SQLException());
//
//        profileServlet.doGet(request, response);
//
//        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        verify(writer).write("Error retrieving profile.");
//    }
//
//    @Test
//    void testDoPost_InternalServerError() throws Exception {
//        ProfileDto profileDto =   new ProfileDto(1, "John", "Doe", "john.doe@example.com", 1);
//        String profileJson = objectMapper.writeValueAsString(profileDto);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(profileJson.getBytes())));
//        when(request.getReader()).thenReturn(reader);
//        when(profileService.saveProfile(any(ProfileDto.class))).thenThrow(new SQLException());
//
//        profileServlet.doPost(request, response);
//
//        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        verify(writer).write("Error created profile.");
//    }
//
//
//}
