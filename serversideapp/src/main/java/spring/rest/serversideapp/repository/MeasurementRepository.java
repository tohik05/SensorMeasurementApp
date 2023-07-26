package spring.rest.serversideapp.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.rest.serversideapp.model.Measurement;
import spring.rest.serversideapp.model.Sensor;

import java.util.List;
import java.util.Optional;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {

    List<Measurement> findAllByIsRainingAndSensor(@NotNull Boolean isRaining, @NotNull Sensor sensor);

}
