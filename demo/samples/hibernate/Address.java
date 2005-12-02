import java.util.Set;

public class Address
{
	Long id;

	String firstName;

	String lastName;

	String street;

	String city;

	Set documents;

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public Long getId()
	{
		return id;
	}

	protected void setId(Long id)
	{
		this.id = id;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getStreet()
	{
		return street;
	}

	public void setStreet(String street)
	{
		this.street = street;
	}

	public Set getDocuments()
	{
		return documents;
	}

	public void setDocuments(Set documents)
	{
		this.documents = documents;
	}

	public String toString()
	{
		return id + ":" + firstName + "," + lastName + "," + street + "," + city;
	}
}
