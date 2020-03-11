package me.hotsse.vueslack.core.component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import me.hotsse.vueslack.emp.vo.EmployeeVO;

@Component
public class SessionManageComponent {
		
	private Map<UUID, EmployeeVO> storage = new HashMap<UUID, EmployeeVO>();
	
	public UUID setEmployee(EmployeeVO empInfo) {
		
		UUID uuid = UUID.randomUUID();
		storage.put(uuid, empInfo);
		
		return uuid;
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
