package me.hotsse.vueslack.core.component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import me.hotsse.vueslack.emp.vo.EmployeeVO;

@Component
public class SessionManageComponent {
		
	private Map<UUID, EmployeeVO> storage = new HashMap<UUID, EmployeeVO>();
	
	public EmployeeVO setEmployee(EmployeeVO empInfo) {
		
		UUID uuid = UUID.randomUUID();
		empInfo.setToken(uuid);		
		storage.put(uuid, empInfo);
		
		return empInfo;
	}
	
	public EmployeeVO getEmployee(UUID uuid) {
		EmployeeVO empInfo = null;
		
		try {
			empInfo = storage.get(uuid);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return empInfo;		
	}
	
	public void invalidateEmployee(UUID uuid) {
		storage.remove(uuid);
	}
	
}
