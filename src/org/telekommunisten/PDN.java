package org.telekommunisten;

public class PDN {

	private String pstn;
	private String description;
	
	public PDN (String pstn, String description)
	{
		this.pstn = pstn;
		this.description = description;
	}

	public String getPstn() {
		return pstn;
	}

	public void setPstn(String pstn) {
		this.pstn = pstn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

//* pstn_number: "4930200090301"
//    * updated_at: "2010/04/24 07:56:06 -0500"
//    * destination: "491805194195"
//    * id: 1199
//    * description: "Deutsche Bahn"
//    * user_id: 394
//    * created_at: "2010/04/24 07:55:32 -0500"