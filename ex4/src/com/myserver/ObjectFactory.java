
package com.myserver;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.myserver package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _QueryUser_QNAME = new QName("http://www.myServer.com", "queryUser");
    private final static QName _AddUserResponse_QNAME = new QName("http://www.myServer.com", "addUserResponse");
    private final static QName _AddProjectResponse_QNAME = new QName("http://www.myServer.com", "addProjectResponse");
    private final static QName _ClearProject_QNAME = new QName("http://www.myServer.com", "clearProject");
    private final static QName _QueryProject_QNAME = new QName("http://www.myServer.com", "queryProject");
    private final static QName _DeleteProjectResponse_QNAME = new QName("http://www.myServer.com", "deleteProjectResponse");
    private final static QName _QueryProjectResponse_QNAME = new QName("http://www.myServer.com", "queryProjectResponse");
    private final static QName _AddUser_QNAME = new QName("http://www.myServer.com", "addUser");
    private final static QName _AddProject_QNAME = new QName("http://www.myServer.com", "addProject");
    private final static QName _DeleteProject_QNAME = new QName("http://www.myServer.com", "deleteProject");
    private final static QName _ClearProjectResponse_QNAME = new QName("http://www.myServer.com", "clearProjectResponse");
    private final static QName _QueryUserResponse_QNAME = new QName("http://www.myServer.com", "queryUserResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.myserver
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link QueryUserResponse }
     * 
     */
    public QueryUserResponse createQueryUserResponse() {
        return new QueryUserResponse();
    }

    /**
     * Create an instance of {@link ClearProjectResponse }
     * 
     */
    public ClearProjectResponse createClearProjectResponse() {
        return new ClearProjectResponse();
    }

    /**
     * Create an instance of {@link AddProject }
     * 
     */
    public AddProject createAddProject() {
        return new AddProject();
    }

    /**
     * Create an instance of {@link DeleteProject }
     * 
     */
    public DeleteProject createDeleteProject() {
        return new DeleteProject();
    }

    /**
     * Create an instance of {@link AddUser }
     * 
     */
    public AddUser createAddUser() {
        return new AddUser();
    }

    /**
     * Create an instance of {@link ClearProject }
     * 
     */
    public ClearProject createClearProject() {
        return new ClearProject();
    }

    /**
     * Create an instance of {@link QueryProject }
     * 
     */
    public QueryProject createQueryProject() {
        return new QueryProject();
    }

    /**
     * Create an instance of {@link DeleteProjectResponse }
     * 
     */
    public DeleteProjectResponse createDeleteProjectResponse() {
        return new DeleteProjectResponse();
    }

    /**
     * Create an instance of {@link QueryProjectResponse }
     * 
     */
    public QueryProjectResponse createQueryProjectResponse() {
        return new QueryProjectResponse();
    }

    /**
     * Create an instance of {@link AddUserResponse }
     * 
     */
    public AddUserResponse createAddUserResponse() {
        return new AddUserResponse();
    }

    /**
     * Create an instance of {@link AddProjectResponse }
     * 
     */
    public AddProjectResponse createAddProjectResponse() {
        return new AddProjectResponse();
    }

    /**
     * Create an instance of {@link QueryUser }
     * 
     */
    public QueryUser createQueryUser() {
        return new QueryUser();
    }

    /**
     * Create an instance of {@link Project }
     * 
     */
    public Project createProject() {
        return new Project();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.myServer.com", name = "queryUser")
    public JAXBElement<QueryUser> createQueryUser(QueryUser value) {
        return new JAXBElement<QueryUser>(_QueryUser_QNAME, QueryUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.myServer.com", name = "addUserResponse")
    public JAXBElement<AddUserResponse> createAddUserResponse(AddUserResponse value) {
        return new JAXBElement<AddUserResponse>(_AddUserResponse_QNAME, AddUserResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.myServer.com", name = "addProjectResponse")
    public JAXBElement<AddProjectResponse> createAddProjectResponse(AddProjectResponse value) {
        return new JAXBElement<AddProjectResponse>(_AddProjectResponse_QNAME, AddProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClearProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.myServer.com", name = "clearProject")
    public JAXBElement<ClearProject> createClearProject(ClearProject value) {
        return new JAXBElement<ClearProject>(_ClearProject_QNAME, ClearProject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.myServer.com", name = "queryProject")
    public JAXBElement<QueryProject> createQueryProject(QueryProject value) {
        return new JAXBElement<QueryProject>(_QueryProject_QNAME, QueryProject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.myServer.com", name = "deleteProjectResponse")
    public JAXBElement<DeleteProjectResponse> createDeleteProjectResponse(DeleteProjectResponse value) {
        return new JAXBElement<DeleteProjectResponse>(_DeleteProjectResponse_QNAME, DeleteProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.myServer.com", name = "queryProjectResponse")
    public JAXBElement<QueryProjectResponse> createQueryProjectResponse(QueryProjectResponse value) {
        return new JAXBElement<QueryProjectResponse>(_QueryProjectResponse_QNAME, QueryProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddUser }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.myServer.com", name = "addUser")
    public JAXBElement<AddUser> createAddUser(AddUser value) {
        return new JAXBElement<AddUser>(_AddUser_QNAME, AddUser.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.myServer.com", name = "addProject")
    public JAXBElement<AddProject> createAddProject(AddProject value) {
        return new JAXBElement<AddProject>(_AddProject_QNAME, AddProject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteProject }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.myServer.com", name = "deleteProject")
    public JAXBElement<DeleteProject> createDeleteProject(DeleteProject value) {
        return new JAXBElement<DeleteProject>(_DeleteProject_QNAME, DeleteProject.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClearProjectResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.myServer.com", name = "clearProjectResponse")
    public JAXBElement<ClearProjectResponse> createClearProjectResponse(ClearProjectResponse value) {
        return new JAXBElement<ClearProjectResponse>(_ClearProjectResponse_QNAME, ClearProjectResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QueryUserResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.myServer.com", name = "queryUserResponse")
    public JAXBElement<QueryUserResponse> createQueryUserResponse(QueryUserResponse value) {
        return new JAXBElement<QueryUserResponse>(_QueryUserResponse_QNAME, QueryUserResponse.class, null, value);
    }

}
