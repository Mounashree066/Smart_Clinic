import React, {
  useEffect,
  useState
} from "react";

import {
  ToastContainer,
  toast
} from "react-toastify";

import "react-toastify/dist/ReactToastify.css";

function Appointments() {

  // =========================
  // STATES
  // =========================

  const [appointments, setAppointments] =
    useState([]);

  const [page, setPage] =
    useState(0);

  const [loading, setLoading] =
    useState(false);

  // =========================
  // FETCH APPOINTMENTS
  // =========================

  const fetchAppointments =
    async () => {

    try {

      setLoading(true);

      const res = await fetch(

        `http://localhost:8082/appointments?page=${page}&size=5`
      );

      const data =
        await res.json();

      setAppointments(
        data.data || []
      );

    }

    catch (error) {

      console.log(error);

      toast.error(
        "Failed to fetch appointments ❌"
      );
    }

    finally {

      setLoading(false);
    }
  };

  // =========================
  // LOAD DATA
  // =========================

  useEffect(() => {

    fetchAppointments();

  }, [page]);

  // =========================
  // DELETE APPOINTMENT
  // =========================

  const handleDelete =
    async (id) => {

    try {

      const res = await fetch(

        `http://localhost:8082/appointments/${id}`,
        {
          method: "DELETE",
        }
      );

      if (res.ok) {

        toast.success(
          "Appointment Deleted ✅"
        );

        fetchAppointments();

      }

      else {

        toast.error(
          "Delete Failed ❌"
        );
      }

    }

    catch (error) {

      console.log(error);

      toast.error(
        "Delete Error ❌"
      );
    }
  };

  // =========================
  // LOGOUT
  // =========================

  const handleLogout = () => {

    localStorage.clear();

    window.location.href = "/";
  };

  return (

    <div
      className="container-fluid min-vh-100 p-5"
      style={{
        background:
          "linear-gradient(to right, #4facfe, #00f2fe)",
      }}
    >

      {/* HEADER */}

      <div className="d-flex justify-content-between align-items-center mb-4">

        <div>

          <h1 className="fw-bold text-white">
            📋 Appointments Dashboard
          </h1>

          <p className="text-white">
            Smart Clinic Management System
          </p>

        </div>

        {/* LOGOUT */}

        <button
          className="btn btn-danger btn-lg rounded-pill px-4"
          onClick={handleLogout}
        >
          Logout
        </button>

      </div>

      {/* TABLE */}

      <div className="card shadow-lg border-0 p-4 rounded-4">

        <h2 className="mb-4 text-primary">
          All Appointments
        </h2>

        {/* LOADING */}

        {
          loading
          ? (

            <div className="text-center">

              <h5>
                Loading appointments...
              </h5>

            </div>
          )

          : (

            <table className="table table-hover">

              <thead className="table-dark">

                <tr>

                  <th>Patient</th>

                  <th>Doctor</th>

                  <th>Specialization</th>

                  <th>Priority</th>

                  <th>Wait Time</th>

                  <th>Date</th>

                  <th>Time</th>

                  <th>Actions</th>

                </tr>

              </thead>

              <tbody>

                {
                  appointments.length > 0

                  ? (

                    appointments.map((a) => (

                      <tr key={a.id}>

                        {/* PATIENT */}

                        <td>
                          {a.name}
                        </td>

                        {/* DOCTOR */}

                        <td>
                          {a.doctor}
                        </td>

                        {/* SPECIALIZATION */}

                        <td>
                          {a.specialization}
                        </td>

                        {/* PRIORITY */}

                        <td>

                          <span
                            className={

                              a.priority ===
                              "Emergency"

                              ? "badge bg-danger"

                              : "badge bg-success"
                            }
                          >

                            {a.priority}

                          </span>

                        </td>

                        {/* WAIT TIME */}

                        <td>
                          {a.waitTime} mins
                        </td>

                        {/* DATE */}

                        <td>
                          {a.date}
                        </td>

                        {/* TIME */}

                        <td>
                          {a.time}
                        </td>

                        {/* ACTION */}

                        <td>

                          <button
                            className="btn btn-danger btn-sm"
                            onClick={() =>
                              handleDelete(
                                a.id
                              )
                            }
                          >
                            Delete
                          </button>

                        </td>

                      </tr>
                    ))

                  )

                  : (

                    <tr>

                      <td
                        colSpan="8"
                        className="text-center"
                      >
                        No appointments found
                      </td>

                    </tr>
                  )
                }

              </tbody>

            </table>
          )
        }

      </div>

      {/* PAGINATION */}

      <div className="d-flex justify-content-center mt-4">

        <button
          className="btn btn-dark me-3"
          onClick={() =>
            setPage(page - 1)
          }
          disabled={page === 0}
        >
          Prev
        </button>

        <h5 className="text-white mt-2">
          Page {page + 1}
        </h5>

        <button
          className="btn btn-dark ms-3"
          onClick={() =>
            setPage(page + 1)
          }
        >
          Next
        </button>

      </div>

      {/* TOAST */}

      <ToastContainer
        position="top-right"
        autoClose={3000}
      />

    </div>
  );
}

export default Appointments;