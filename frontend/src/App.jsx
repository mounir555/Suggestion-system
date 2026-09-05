import { useState, useEffect } from 'react'
import axios from 'axios'

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [user, setUser] = useState(null);
  const [suggestions, setSuggestions] = useState([]);
  const [newSuggestion, setNewSuggestion] = useState({ title: '', description: '' });
  const [credentials, setCredentials] = useState({ username: '', password: '' });
  const [selectedFile, setSelectedFile] = useState(null);
  const [adminFile, setAdminFile] = useState(null);

  const API_BASE = "http://localhost:8080/api";

  const fetchProfile = async () => {
    const token = localStorage.getItem('userToken');
    try {
      const response = await axios.get(`${API_BASE}/users/me`, {
        headers: { 'Authorization': `Basic ${token}` }
      });
      setUser(response.data);
    } catch (error) {
      console.error("Error fetching profile")
    }
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    const token = btoa(credentials.username + ":" + credentials.password);
    try {
      const response = await axios.get(`${API_BASE}/users/me`, {
        headers: { 'Authorization': `Basic ${token}` }
      });
      localStorage.setItem('userToken', token);
      setUser(response.data);
      setIsLoggedIn(true);
    } catch (error) {
      alert("Invalid login");
    }
  };

  const loadData = async () => {
    const token = localStorage.getItem('userToken');
    const url = user?.role === 'ADMIN' ? `${API_BASE}/suggestions/all` : `${API_BASE}/suggestions/my`;
    try {
      const response = await axios.get(url, {
        headers: { 'Authorization': `Basic ${token}` }
      });
      setSuggestions(response.data);
    } catch (error) { console.error("Erreur data"); }
  };

  const handleDecision = async (id, decision) => {
    const token = localStorage.getItem('userToken');
    const data = new FormData();
    if (adminFile) data.append("file", adminFile);
    try {
      await axios.put(`${API_BASE}/suggestions/${id}/${decision}`, {}, {
        headers: { 'Authorization': `Basic ${token}` }
      });
      setAdminFile(null);
      loadData();
    } catch (error) { console.error("Erreur decision"); }
  };

  const submitSuggestion = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem('userToken');

    const data = new FormData();
    data.append("title", newSuggestion.title);
    data.append("description", newSuggestion.description);

    if (selectedFile) {
      data.append("file", selectedFile);
    }
    try {
      await axios.post(`${API_BASE}/suggestions`, data, {
        headers: { 'Authorization': `Basic ${token}`, 'Content-Type': 'multipart/form-data' }
      });
      setNewSuggestion({ title: '', description: '' });
      setSelectedFile(null);
      loadData();
      alert("Suggestion sent succefully!");
    } catch (error) {
      console.error("Upload failed", error);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('userToken');
    setIsLoggedIn(false);
    setUser(null);
    setSuggestions([]);
  };

  useEffect(() => {
    if (isLoggedIn) {
      loadData();
    }
  }, [isLoggedIn, user?.role]);

  return (
    <div className="container mt-4">
      {!isLoggedIn ? (
        <div className="card p-4 mx-auto" style={{ maxWidth: '400px' }}>
          <h3>Login</h3>
          <form onSubmit={handleLogin}>
            <input
              type="text" className="form-control mb-2" placeholder="User"
              onChange={(e) => setCredentials({ ...credentials, username: e.target.value })}
            />
            <input
              type="password" className="form-control mb-2" placeholder="Pass"
              onChange={(e) => setCredentials({ ...credentials, password: e.target.value })}
            />
            <button className="btn btn-primary w-100">Login</button>
          </form>
        </div>
      ) : (
        <>
          <nav className="navbar navbar-expand-lg navbar-dark bg-dark p-3 mb-4 rounded shadow">
            <div className="container-fluid">
              <span className="navbar-brand">💡 SuggestionBox</span>
              <div className="ms-auto d-flex align-items-center">
                <span className="text-white me-3">
                  {user?.role === 'ADMIN' ? "Mode: ADMIN" : `🏆 Points: ${user?.points}`}
                </span>
                <button className="btn btn-outline-light btn-sm" onClick={handleLogout}>Logout</button>
              </div>
            </div>
          </nav>

          {user?.role === 'USER' && (
            <div className="card p-3 mb-4 shadow-sm border-primary">
              <h5><i className="bi bi-plus-circle"></i> New Suggestion</h5>
              <form onSubmit={submitSuggestion}>
                <input
                  type="file"
                  className="form-control mb-2"
                  onChange={(e) => setSelectedFile(e.target.files[0])}
                />
                <input
                  required
                  type="text" className="form-control mb-2" placeholder="Title"
                  value={newSuggestion.title}
                  onChange={(e) => setNewSuggestion({ ...newSuggestion, title: e.target.value })}
                />
                <textarea
                  required
                  className="form-control mb-2" placeholder="Describe your idea..."
                  value={newSuggestion.description}
                  onChange={(e) => setNewSuggestion({ ...newSuggestion, description: e.target.value })}
                />
                <button type="submit" className="btn btn-success w-100">Submit Idea</button>
              </form>
            </div>
          )}

          <h4 className="mb-3">{user?.role === 'ADMIN' ? "All Suggestions" : "My Suggestions"}</h4>
          <table className="table table-striped shadow-sm">
            <thead className="table-dark">
              <tr>
                <th>Titre de la Suggestion</th>
                <th>Description</th>
                <th>Date de Dépôt</th>
                <th>Statut</th>
                <th>Traité par</th>
                <th>Date de Traitement</th>
                <th>Pièces Jointes</th>
              </tr>
            </thead>
            <tbody>
              {Array.isArray(suggestions) && suggestions.map(s => (
                <tr key={s.id}>
                  {/* 1. Titre */}
                  <td>{s.title}</td>

                  {/* 2. Description */}
                  <td>{s.description}</td>

                  {/* 3. Date de Dépôt */}
                  <td>{s.createdAt ? new Date(s.createdAt).toLocaleString() : "-"}</td>

                  {/* 4. Statut */}
                  <td>
                    <span className={`badge ${s.status === 'ACCEPTED' ? 'bg-success' : s.status === 'REJECTED' ? 'bg-danger' : 'bg-warning'}`}>
                      {s.status}
                    </span>
                  </td>

                  {/* 5. Traité par */}
                  <td>{s.processedBy ? s.processedBy.username : "-"}</td>

                  {/* 6. Date de Traitement */}
                  <td>{s.processedAt ? new Date(s.processedAt).toLocaleString() : "-"}</td>

                  {/* 7. Actions / Pièces Jointes */}
                  <td>
                    {/* Liens vers les fichiers */}
                    {s.presentationFileName && (
                      <a href={`http://localhost:8080/files/${s.presentationFileName}`} target="_blank" className="btn btn-link btn-sm p-0 me-2">
                        <i className="bi bi-file-earmark-pdf"></i> Prés.
                      </a>
                    )}
                    {s.implementationFileName && (
                      <a href={`http://localhost:8080/files/${s.implementationFileName}`} target="_blank" className="btn btn-link btn-sm p-0 text-success">
                        <i className="bi bi-file-earmark-check"></i> Impl.
                      </a>
                    )}

                    {/* Boutons Admin */}
                    {user?.role === 'ADMIN' && s.status === 'PENDING' && (
                      <div className="mt-2">
                        <button className="btn btn-success btn-sm me-1" onClick={() => handleDecision(s.id, 'accept')}>Accept</button>
                        <button className="btn btn-danger btn-sm" onClick={() => handleDecision(s.id, 'reject')}>Reject</button>
                      </div>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </>
      )}
    </div>
  );
}

export default App;