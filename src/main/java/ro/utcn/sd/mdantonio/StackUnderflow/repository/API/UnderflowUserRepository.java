package ro.utcn.sd.mdantonio.StackUnderflow.repository.API;

import ro.utcn.sd.mdantonio.StackUnderflow.entities.UnderflowUser;

import java.util.Optional;

public interface UnderflowUserRepository extends CRUDQOperations<UnderflowUser> {
    public Optional<UnderflowUser> findByUsername(String username);
}
